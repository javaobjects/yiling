package com.yiling.order.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.bo.OrderDetailBatchBO;
import com.yiling.order.order.bo.OrderModifyAuditChangeBO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderInvoiceApplyDTO;
import com.yiling.order.order.dto.OrderReturnDTO;
import com.yiling.order.order.dto.request.DeliveryInfoRequest;
import com.yiling.order.order.dto.request.ModifyOrderNotAuditRequest;
import com.yiling.order.order.dto.request.OrderDeliveryRequest;
import com.yiling.order.order.dto.request.OrderDetailRequest;
import com.yiling.order.order.dto.request.OrderReturnApplyRequest;
import com.yiling.order.order.dto.request.SaveOrderDeliveryInfoRequest;
import com.yiling.order.order.dto.request.UpdateOrderNotAuditRequest;
import com.yiling.order.order.entity.OrderDO;
import com.yiling.order.order.entity.OrderDeliveryDO;
import com.yiling.order.order.entity.OrderDeliveryErpDO;
import com.yiling.order.order.entity.OrderDeliveryReceivableDO;
import com.yiling.order.order.entity.OrderDetailChangeDO;
import com.yiling.order.order.entity.OrderDetailDO;
import com.yiling.order.order.entity.OrderReturnDetailDO;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderLogTypeEnum;
import com.yiling.order.order.enums.OrderReturnTypeEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.service.OrderDeliveryErpService;
import com.yiling.order.order.service.OrderDeliveryReceivableService;
import com.yiling.order.order.service.OrderDeliveryService;
import com.yiling.order.order.service.OrderDetailChangeService;
import com.yiling.order.order.service.OrderDetailService;
import com.yiling.order.order.service.OrderInvoiceApplyService;
import com.yiling.order.order.service.OrderLogService;
import com.yiling.order.order.service.OrderModifyAuditService;
import com.yiling.order.order.service.OrderReturnDetailService;
import com.yiling.order.order.service.OrderReturnService;
import com.yiling.order.order.service.OrderService;
import com.yiling.order.order.service.ReturnService;
import com.yiling.order.order.util.OrderUtil;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单反审核
 *
 * @author zhigang.guo
 * @date: 2021/8/6
 */
@Slf4j
@Component
public class OrderModifyAuditServiceImpl implements OrderModifyAuditService {

    @Autowired
    private OrderService             orderService;

    @Autowired
    private OrderDeliveryService     orderDeliveryService;
    @Autowired
    private OrderDeliveryErpService orderDeliveryErpService;

    @Autowired
    private OrderReturnService       orderReturnService;

    @Autowired
    private OrderReturnDetailService orderReturnDetailService;

    @Autowired
    private RedisDistributedLock     redisDistributedLock;

    @Autowired
    private OrderDetailChangeService orderDetailChangeService;

    @Autowired
    private OrderDetailService       orderDetailService;

    @Autowired
    private OrderLogService                orderLogService;
    @Autowired
    private OrderDeliveryReceivableService orderDeliveryReceivableService;
    @Autowired
    private OrderInvoiceApplyService       orderInvoiceApplyService;

    @Resource(name = "popReturnService")
    private ReturnService popReturnService;

    /**
     * 获取redisName
     *
     * @param orderNo
     * @return
     */
    private String getLockName(String orderNo) {

        StringBuilder sb = new StringBuilder();

        sb.append(OrderUtil.UNRECEIVEERPONLINELOCK).append(Constants.SEPARATOR_MIDDLELINE).append(orderNo);

        return sb.toString();
    }

    /**********************
     * 订单反审核
     * 1,返审订单必须是在发货节点或者收货节点发起，并且订单必须是未开票的(不需要开票也不行）
     * 2,返审的发货数量必须小于等于订单的购买数量
     * 3,已发货状态下，返审可以存在新的批次返审；
     * 4,已收货情况，不容许有新的批次，否则提示（返审核后导致退回退货单据里发货批次号变更或者数量减少为负数）
     *  a,已收货的情况下，返审，如果发货数量变大，增加实际收货数量,退回退货单数量保持不变
     *  b,已收货的情况下，返审，如果发货数量变小，实际收货数量保持不变,退回退货减少
     *  c,如果退回退货数量减至为0则该退货单逻辑删除
     *  d,如果退回退货数量减至为负数 则提示(返审核后导致退回退货单据里发货批次号变更或者数量减少为负数)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "订单反审核", businessType = BusinessTypeEnum.UPDATE, save = true)
    public Result<OrderModifyAuditChangeBO> modifyOrderNotAudit(UpdateOrderNotAuditRequest updateOrderNotAuditRequest) {


        log.info(".modifyOrderNotAudit:{}", JSON.toJSON(updateOrderNotAuditRequest));

        OrderDTO orderDTO = orderService.getOrderInfo(updateOrderNotAuditRequest.getOrderId());
        //是否需要退回账期
        Boolean isReducePaymentAccount = PaymentMethodEnum.PAYMENT_DAYS.getCode().equals(orderDTO.getPaymentMethod().longValue());
        // 校验反审核订单状态
        Map<Long, OrderDetailDO> detailDOMap = this.validateOrderStatus(updateOrderNotAuditRequest, orderDTO);
        //获取锁ID
        String lockId = "";
        //锁名称
        String lockName = this.getLockName(orderDTO.getOrderNo());
        // 退款总金额
        BigDecimal returnTotalAmount = BigDecimal.ZERO;

        OrderModifyAuditChangeBO changeBO = new OrderModifyAuditChangeBO();

        try {
            lockId = redisDistributedLock.lock(lockName, 60, 120, TimeUnit.SECONDS);

            if (StringUtils.isEmpty(lockId)) {

                throw new BusinessException(OrderErrorCode.ORDER_NOT_AUDIT_LOCK_INCORRECT, "系统繁忙，获取锁失败，orderNo:" + orderDTO.getOrderNo());
            }
            // 查询反审前的发货单
            List<OrderDeliveryDO> beforeDeliverys = orderDeliveryService.getOrderDeliveryList(updateOrderNotAuditRequest.getOrderId());

            if (CollectionUtil.isEmpty(beforeDeliverys)) {

                throw new BusinessException(OrderErrorCode.ORDER_INFO_PARAM_ERROR);
            }
            //发货批次关键字
            Set<String> deliveryBatchkeys = beforeDeliverys.stream()
                .map(e -> e.getDetailId() + Constants.SEPARATOR_MIDDLELINE + e.getGoodsId() + Constants.SEPARATOR_MIDDLELINE + e.getBatchNo())
                .collect(Collectors.toSet());
            // 修改前发货数量
            Map<String, Integer> beforeDeliveryQtyMap = beforeDeliverys.stream()
                .collect(Collectors.toMap(
                    t -> t.getDetailId() + Constants.SEPARATOR_MIDDLELINE + t.getGoodsId() + Constants.SEPARATOR_MIDDLELINE + t.getBatchNo(),
                    e -> e.getDeliveryQuantity()));
            // 修改前收货数量
            Map<String, Integer> beforeReceiveQtyMap = beforeDeliverys.stream()
                .collect(Collectors.toMap(
                    t -> t.getDetailId() + Constants.SEPARATOR_MIDDLELINE + t.getGoodsId() + Constants.SEPARATOR_MIDDLELINE + t.getBatchNo(),
                    e -> e.getReceiveQuantity()));

            List<SaveOrderDeliveryInfoRequest> deliveryInfoRequestList = updateOrderNotAuditRequest.getOrderDeliveryGoodsInfoList();
            // 当前发货明细个数
            Integer currentDeliveryDetailSize = deliveryInfoRequestList.stream().collect(Collectors.summingInt(e -> e.getDeliveryInfoList().size()));

            //订单发货数据
            Map<Long, Integer> beforeDelivery = beforeDeliverys.stream()
                .collect(Collectors.groupingBy(OrderDeliveryDO::getDetailId, Collectors.summingInt(OrderDeliveryDO::getDeliveryQuantity)));
            List<OrderDeliveryDO> deliveryList = new ArrayList<>();
            List<OrderDeliveryErpDO> orderDeliverErpList = new ArrayList<>();
            Set<String> erpDeliveryNos = Sets.newHashSet();
            List<OrderDetailDO> detailList = Lists.newArrayList();
            //当前发货数量
            Map<Long, Integer> currentDelivery = Maps.newHashMap();
            // 当前收货数量
            Map<Long, Integer> currentReceive = Maps.newHashMap();
            List<String> errorBatchList = Lists.newArrayList();
            // 校验反审后，是否有删除的发货批次信息
            List<String> errorBatchs = this.checkErrorBatchs(orderDTO,deliveryInfoRequestList,beforeDeliverys,beforeDeliveryQtyMap,beforeDeliveryQtyMap);
            errorBatchList.addAll(errorBatchs);

            // 校验整个反审是否发生了数据变更
            Integer changeDeliveryCount = 0;

            for (SaveOrderDeliveryInfoRequest e : deliveryInfoRequestList) {

                OrderDetailDO orderDetailDO = detailDOMap.get(e.getDetailId());
                orderDetailDO.setOpTime(updateOrderNotAuditRequest.getOpTime());
                orderDetailDO.setOpUserId(updateOrderNotAuditRequest.getOpUserId());

                // 订单明细发货数量
                Integer deliveryQuantity = 0;
                // 收货数量
                Integer receiveQty = 0;
                List<DeliveryInfoRequest> batchDeliverInfoList = e.getDeliveryInfoList();

                for (DeliveryInfoRequest one : batchDeliverInfoList) {
                    // erp 出库单信息
                    OrderDeliveryErpDO orderDeliveryErpDO = new OrderDeliveryErpDO();
                    orderDeliveryErpDO.setBatchNo(one.getBatchNo());
                    orderDeliveryErpDO.setErpDeliveryNo(one.getErpDeliveryNo());
                    orderDeliveryErpDO.setDetailId(e.getDetailId());
                    orderDeliveryErpDO.setDeliveryQuantity(one.getDeliveryQuantity());
                    orderDeliveryErpDO.setOrderId(orderDTO.getId());
                    orderDeliveryErpDO.setExpiryDate(one.getExpiryDate());
                    orderDeliveryErpDO.setErpSendOrderId(one.getEasSendOrderId());
                    orderDeliverErpList.add(orderDeliveryErpDO);
                    erpDeliveryNos.add(one.getErpDeliveryNo());
                }

                Map<String,List<DeliveryInfoRequest>> batchDeliverInfoMap = batchDeliverInfoList.stream().collect(Collectors.groupingBy(DeliveryInfoRequest::getBatchNo));

                for (String batchNo : batchDeliverInfoMap.keySet()) {
                    Integer batchDeliveryQuantity = batchDeliverInfoMap.get(batchNo).stream().collect(Collectors.summingInt(DeliveryInfoRequest::getDeliveryQuantity));

                    // 发货批次信息
                    OrderDetailBatchBO orderDetailBatchBo = OrderDetailBatchBO.builder().detailId(e.getDetailId()).goodsId(e.getGoodsId())
                            .batchNo(batchNo).build();

                    // 修改前发货数量
                    Integer beforeDeliveryQty = Optional.ofNullable(OrderUtil.getQtyFromResultMap(beforeDeliveryQtyMap, orderDetailBatchBo))
                            .orElse(0);
                    // 修改前收货数量
                    Integer beforeReceiveQty = Optional.ofNullable(OrderUtil.getQtyFromResultMap(beforeReceiveQtyMap, orderDetailBatchBo)).orElse(0);

                    // 比较发货数量是否发生变更,如果前后变更数量一致，无需处理退货逻辑
                    if (!beforeDeliveryQty.equals(batchDeliveryQuantity)) {

                        changeDeliveryCount++;
                    }

                    // 小于0的无需处理
                    if (batchDeliveryQuantity.compareTo(0) <= 0) {

                        continue;
                    }

                    DeliveryInfoRequest one = batchDeliverInfoMap.get(batchNo).stream().findFirst().get();
                    one.setDeliveryQuantity(batchDeliveryQuantity);

                    OrderDeliveryDO delivery = PojoUtils.map(one, OrderDeliveryDO.class);
                    delivery.setOrderId(updateOrderNotAuditRequest.getOrderId());
                    delivery.setGoodsId(orderDetailDO.getGoodsId());
                    delivery.setDetailId(orderDetailDO.getId());
                    delivery.setStandardId(orderDetailDO.getStandardId());
                    delivery.setGoodsQuantity(orderDetailDO.getGoodsQuantity());
                    delivery.setOpUserId(orderDetailDO.getOpUserId());
                    delivery.setOpTime(updateOrderNotAuditRequest.getOpTime());
                    delivery.setGoodsErpCode(orderDetailDO.getGoodsErpCode());
                    delivery.setReceiveQuantity(0);

                    // 已审核
                    if (OrderStatusEnum.DELIVERED.getCode().equals(orderDTO.getOrderStatus())) {

                        deliveryQuantity = deliveryQuantity + batchDeliveryQuantity;
                        deliveryList.add(delivery);
                        continue;
                    }

                    // 破损发货数量
                    Integer deleteQty = beforeDeliveryQty - beforeReceiveQty;
                    //收货数量
                    Integer receiveQuantity = beforeReceiveQty;

                    // 如果是收货完成反审核，需要校验，新的发货批次是否存在老的发货批次中，如果不存在应该提示异常
                    if (!deliveryBatchkeys.contains(orderDetailBatchBo.getBatchKey())) {

                        errorBatchList.add(batchNo);
                        deliveryList.add(delivery);
                        continue;
                    }

                    // 表示发货数量大于开始收货数量
                    if (batchDeliveryQuantity.compareTo(beforeDeliveryQty) > 0) {
                        // 扣减差值
                        Integer diffDeliveryQty = batchDeliveryQuantity - beforeDeliveryQty;
                        receiveQuantity = receiveQuantity + diffDeliveryQty;
                        delivery.setReceiveQuantity(receiveQuantity);
                        deliveryQuantity = deliveryQuantity + batchDeliveryQuantity;
                        receiveQty = receiveQty + delivery.getReceiveQuantity();
                        deliveryList.add(delivery);

                        continue;
                    }

                    // 扣减差值
                    Integer diffDeliveryQty = beforeDeliveryQty - batchDeliveryQuantity;
                    // 表示发货数量减,扣减破损，实发数量不发生变化
                    if (deleteQty == null || deleteQty.compareTo(diffDeliveryQty) < 0) {

                        errorBatchList.add(batchNo);
                        deliveryList.add(delivery);
                        continue;
                    }
                    if (diffDeliveryQty.compareTo(deleteQty) == 1) {
                        receiveQuantity = receiveQuantity - (diffDeliveryQty - deleteQty);
                    }

                    delivery.setReceiveQuantity(receiveQuantity);
                    deliveryQuantity = deliveryQuantity + batchDeliveryQuantity;
                    receiveQty = receiveQty + delivery.getReceiveQuantity();

                    deliveryList.add(delivery);
                }

                // 最新一次发货数量
                currentDelivery.put(e.getDetailId(), deliveryQuantity);
                // 最新一次收货数量
                currentReceive.put(e.getDetailId(), receiveQty);
                detailList.add(orderDetailDO);
            }

            // 反审前的发货数量
            Integer erpOrderDeliverySize = Optional.ofNullable(orderDeliveryErpService.selectErporderDeliverySize(orderDTO.getId())).orElse(0);
            //如果整个反审没发生数量变更,不执行退货逻辑
            if (changeDeliveryCount == 0 && currentDeliveryDetailSize.compareTo(erpOrderDeliverySize) == 0) {

                changeBO.setIsProductReturnOrder(false);
                changeBO.setIsReducePaymentAccount(false);
                changeBO.setReturnTotalAmount(returnTotalAmount);
                changeBO.setInventoryChangeMap(Collections.emptyMap());

                return Result.success(changeBO);
            }

            // 修改反审核状态以及反审核时间
            OrderDO orderDO = new OrderDO();
            orderDO.setId(orderDTO.getId());
            orderDO.setAuditModifyFlag(1);
            orderDO.setAuditModifyTime(new Date());

            orderService.updateById(orderDO);

            // 表示反审之后一单都没有发货需要取消整个订单，并作废发票订单
            if (CollectionUtil.isEmpty(deliveryList)) {

                // 是否生成退货单
                changeBO.setIsProductReturnOrder(false);
                changeBO.setIsReducePaymentAccount(isReducePaymentAccount);
                changeBO.setReturnTotalAmount(orderDTO.getTotalAmount().add(orderDTO.getFreightAmount()).subtract(orderDTO.getCashDiscountAmount()));
                changeBO.setInventoryChangeMap(Collections.emptyMap());

                // 整单取消订单
                this.cancelOrder(orderDTO);

                return Result.success(changeBO);
            }

            // 设置是否取消整个订单
            changeBO.setIsProductReturnOrder(true);
            changeBO.setIsReducePaymentAccount(isReducePaymentAccount);

            // 如果发货批次有误需要给出提示
            if (CollectionUtil.isNotEmpty(errorBatchList)) {

                StringBuilder sb = new StringBuilder("批次号:");
                sb.append("[" + StringUtils.join(errorBatchList.toArray(), ",") + "],");
                sb.append("反审核后退回退货单据里发货批次号变更或者数量减少为负数");

                return Result.failed(sb.toString());
            }

            // 清除发货单，退货单数据
            this.clearOrderDeliveryData(updateOrderNotAuditRequest.getOrderId());

            // 设置orderDetailChange数据变更结果，并返回库存变更记录
            Map<Long, Integer> inventoryMap = this.setOrderDetailChange(orderDTO, detailDOMap, currentDelivery, currentReceive, beforeDelivery);

            // 设置库存变动记录
            changeBO.setInventoryChangeMap(inventoryMap);

            // 生成新的发货数据
            orderDeliveryService.saveOrUpdateBatch(deliveryList);

            // 生成erp出库单发货数据
            orderDeliveryErpService.saveOrUpdateBatch(orderDeliverErpList);

            // 生成出库单信息
            if (CollectionUtil.isNotEmpty(erpDeliveryNos)) {
                List<OrderDeliveryReceivableDO> orderDeliveryReceivableDOList = Lists.newArrayList();
                erpDeliveryNos.forEach(e -> {
                    OrderDeliveryReceivableDO orderDeliveryReceivable = new OrderDeliveryReceivableDO();
                    orderDeliveryReceivable.setOrderId(orderDTO.getId());
                    orderDeliveryReceivable.setErpDeliveryNo(e);
                    orderDeliveryReceivable.setErpReceivableFlag(1);
                    orderDeliveryReceivable.setCreateUser(0l);
                    orderDeliveryReceivable.setCreateTime(new Date());

                    orderDeliveryReceivableDOList.add(orderDeliveryReceivable);
                });
                orderDeliveryReceivableService.saveOrUpdateBatch(orderDeliveryReceivableDOList);
            }

            // 修改收货数量以及收货数量
            orderDetailService.updateBatchById(detailList);

            List<OrderDetailChangeDO> orderDetailChangeDOList = orderDetailChangeService.listByOrderId(updateOrderNotAuditRequest.getOrderId());
            returnTotalAmount = orderDetailChangeDOList.stream().map(OrderDetailChangeDO::getSellerReturnAmount).reduce(BigDecimal::add).get();
            BigDecimal returnCashDiscountAmount = orderDetailChangeDOList.stream().map(OrderDetailChangeDO::getSellerReturnCashDiscountAmount).reduce(BigDecimal::add).get();
            returnTotalAmount = returnTotalAmount.subtract(returnCashDiscountAmount);
            changeBO.setReturnTotalAmount(returnTotalAmount);

        } catch (InterruptedException e) {

            return Result.failed("系统繁忙，获取锁失败，orderNo:" + orderDTO.getOrderNo());

        } finally {

            // 记录日志
            orderLogService.add(orderDTO.getId(), OrderLogTypeEnum.ORDER_ORDINARY_LOG.getCode(), "ERP提交反审请求");

            redisDistributedLock.releaseLock(lockName, lockId);
        }

        return Result.success(changeBO);
    }

    /**
     * 校验反审时，是否存在删除的发货批次
     * @param orderDTO
     * @param deliveryInfoRequests
     * @param beforeDeliverys
     * @param beforeDeliveryQtyMap
     * @param beforeReceiveQtyMap
     * @return
     */
    private List<String> checkErrorBatchs(OrderDTO orderDTO,List<SaveOrderDeliveryInfoRequest> deliveryInfoRequests,  List<OrderDeliveryDO> beforeDeliverys,Map<String, Integer> beforeDeliveryQtyMap,Map<String, Integer> beforeReceiveQtyMap) {

        if (OrderStatusEnum.DELIVERED.getCode().equals(orderDTO.getOrderStatus())) {

           return Collections.emptyList();
        }

        List<String> errorBatchList = Lists.newArrayList();

        Set<String> afterDeliveryBatchkeys = Sets.newHashSet();
        for (SaveOrderDeliveryInfoRequest t : deliveryInfoRequests) {
            for (DeliveryInfoRequest one : t.getDeliveryInfoList()) {
                String key = t.getDetailId() + Constants.SEPARATOR_MIDDLELINE + t.getGoodsId() + Constants.SEPARATOR_MIDDLELINE + one.getBatchNo();
                afterDeliveryBatchkeys.add(key);
            }
        }

        for (OrderDeliveryDO e : beforeDeliverys) {
            String  key = e.getDetailId() + Constants.SEPARATOR_MIDDLELINE + e.getGoodsId() + Constants.SEPARATOR_MIDDLELINE + e.getBatchNo();
            if (!afterDeliveryBatchkeys.contains(key)) {

                OrderDetailBatchBO orderDetailBatchBo = OrderDetailBatchBO.builder().detailId(e.getDetailId()).goodsId(e.getGoodsId())
                        .batchNo(e.getBatchNo()).build();
                // 修改前发货数量
                Integer beforeDeliveryQty = Optional.ofNullable(OrderUtil.getQtyFromResultMap(beforeDeliveryQtyMap, orderDetailBatchBo)).orElse(0);
                // 修改前收货数量
                Integer beforeReceiveQty = Optional.ofNullable(OrderUtil.getQtyFromResultMap(beforeReceiveQtyMap, orderDetailBatchBo)).orElse(0);
                // 扣减差值
                Integer diffDeliveryQty = beforeDeliveryQty - beforeReceiveQty;

                if (CompareUtil.compare(beforeReceiveQty,diffDeliveryQty) > 0) {

                    errorBatchList.add(e.getBatchNo());
                }
            }
        }

        return errorBatchList;

    }

    /**
     * 设置orderDetailChange数据变更结果，并返回库存变更记录
     * @param orderDTO
     * @param detailDOMap
     * @param currentDelivery
     * @param currentReceive
     * @param beforeDelivery
     * @return
     */
    private Map<Long, Integer> setOrderDetailChange(OrderDTO orderDTO, Map<Long, OrderDetailDO> detailDOMap, Map<Long, Integer> currentDelivery,
                                                    Map<Long, Integer> currentReceive, Map<Long, Integer> beforeDelivery) {

        // 库存记录变化记录
        Map<Long, Integer> inventoryMap = Maps.newHashMap();

        // 记录发货数量
        for (Long orderDetailId : detailDOMap.keySet()) {

            OrderDetailDO detailDO = detailDOMap.get(orderDetailId);

            // 当前发货数量
            Integer currentDeliveryQty = Optional.ofNullable(currentDelivery.get(orderDetailId)).orElse(detailDO.getGoodsQuantity());

            // 当前收货数量
            Integer currentReceiveQty = Optional.ofNullable(currentReceive.get(orderDetailId)).orElse(detailDO.getGoodsQuantity());

            // 记录发货金额
            orderDetailChangeService.updateDeliveryData(orderDetailId, currentDeliveryQty);

            // 收货状态才有收货记录
            if (OrderStatusEnum.RECEIVED.getCode().equals(orderDTO.getOrderStatus())) {

                // 记录收货金额
                orderDetailChangeService.updateReceiveData(orderDetailId, currentReceiveQty);
            }

            //修改前发货数量
            Integer beforeDeliveryQty = Optional.ofNullable(beforeDelivery.get(orderDetailId)).orElse(0);

            if (beforeDeliveryQty.equals(currentDeliveryQty)) {

                continue;
            }
            // 记录库存变动结果，便于调用方处理库存释放逻辑
            inventoryMap.put(detailDO.getGoodsId(), currentDeliveryQty - beforeDeliveryQty);
        }

        return inventoryMap;

    }

    /**
     * 清除发货单，退货单数据
     *
     * @param orderId
     */
    private void clearOrderDeliveryData(Long orderId) {

        // 清除发货数据
        orderDeliveryService.deleteOrderDelivery(orderId,0l);

        // 反审核清除生成的明细数据
        orderDetailChangeService.clearDeliveryDataByOrderId(orderId);

        // 清除ERP发货数据信息
        orderDeliveryErpService.deleteErpOrderDelivery(orderId,0l);

        // 清除应收单数据
        orderDeliveryReceivableService.deleteOrderDeliveryReceivable(orderId,0l);
    }

    /**
     *
     * 校验订单反审核状态
     *
     * @param updateOrderNotAuditRequest
     * @param orderDTO
     */
    private Map<Long, OrderDetailDO> validateOrderStatus(UpdateOrderNotAuditRequest updateOrderNotAuditRequest, OrderDTO orderDTO) {

        List<Integer> allowOrderNotAuditStatus = Lists.newArrayList();
        allowOrderNotAuditStatus.add(OrderStatusEnum.DELIVERED.getCode());
        allowOrderNotAuditStatus.add(OrderStatusEnum.RECEIVED.getCode());

        // 校验，反审核的订单的状态是否在已发货或者已收货状态
        if (!allowOrderNotAuditStatus.contains(orderDTO.getOrderStatus())) {

            throw new BusinessException(OrderErrorCode.ORDER_NOT_AUDIT_STATUS_INCORRECT);
        }
        List<OrderInvoiceApplyDTO> invoiceApplyDTOList = orderInvoiceApplyService.listByOrderId(orderDTO.getId());
        // 校验是是否为开票状态，否则提示无法反审核
        if (CollectionUtil.isNotEmpty(invoiceApplyDTOList)) {
            
            throw new BusinessException(OrderErrorCode.ORDER_NOT_AUDIT_INVOICE_STATUS_INCORRECT);
        }

        if (CollectionUtil.isEmpty(updateOrderNotAuditRequest.getOrderDeliveryGoodsInfoList())) {

            throw new BusinessException(OrderErrorCode.ORDER_INFO_PARAM_ERROR);
        }

        // 校验参数传递准确性
        for (SaveOrderDeliveryInfoRequest infoRequest : updateOrderNotAuditRequest.getOrderDeliveryGoodsInfoList()) {

            if (CollectionUtil.isEmpty(infoRequest.getDeliveryInfoList())) {

                throw new BusinessException(OrderErrorCode.ORDER_INFO_PARAM_ERROR);
            }
        }

        // 校验发货数量是否和商品购买数量一致
        List<OrderDetailDO> orderDetailList = orderDetailService
            .getOrderDetailByOrderIds(Collections.singletonList(updateOrderNotAuditRequest.getOrderId()));

        /****检验发货数量是否大于购买数量***/
        Map<Long, OrderDetailDO> mapDetail = orderDetailList.stream().collect(Collectors.toMap(OrderDetailDO::getId, one -> one, (k1, k2) -> k1));

        // 校验相同的批次
        Set<String> existsBatchKeys = Sets.newHashSet();
        for (SaveOrderDeliveryInfoRequest deliveryRequest : updateOrderNotAuditRequest.getOrderDeliveryGoodsInfoList()) {

            OrderDetailDO detail = mapDetail.get(deliveryRequest.getDetailId());

            if (detail == null) {

                throw new BusinessException(OrderErrorCode.ORDER_DETAIL_NOT_INFO);
            }

            Integer sumDeliveryQuantity = 0;

            for (DeliveryInfoRequest one : deliveryRequest.getDeliveryInfoList()) {

                // 校验批次是否重复
                boolean isExists = existsBatchKeys.add(deliveryRequest.getDetailId() + Constants.SEPARATOR_MIDDLELINE + deliveryRequest.getGoodsId()
                        + Constants.SEPARATOR_MIDDLELINE  + one.getBatchNo() + Constants.SEPARATOR_MIDDLELINE  + one.getErpDeliveryNo());

                if (!isExists) {

                    throw new BusinessException(OrderErrorCode.ORDER_NOT_AUDIT_STATUS_REPEAT);
                }

                if (one.getDeliveryQuantity() > 0) {

                    if (StringUtils.isEmpty(one.getBatchNo()) || one.getExpiryDate() == null ) {

                        throw new BusinessException(OrderErrorCode.ORDER_INFO_PARAM_ERROR);
                    }
                }

                sumDeliveryQuantity = sumDeliveryQuantity + one.getDeliveryQuantity();
            }

            if (detail.getGoodsQuantity() < sumDeliveryQuantity) {

                throw new BusinessException(OrderErrorCode.ORDER_INFO_PARAM_ERROR);
            }
        }

        return mapDetail;
    }

    @Override
    public boolean insertReturnOrderForModifyAudit(String orderNo) {

        OrderDTO orderDo = orderService.getOrderInfo(orderNo);

        // 清除退货单数据
        orderReturnService.deleteReturnOrder(orderDo.getId());

        // 生成供应商退货单
        this.autoCreateSupplyerApplyReturn(orderDo);

        // 如果已收货需要重新生成破损退货单
        if (OrderStatusEnum.RECEIVED.getCode().equals(orderDo.getOrderStatus())) {
            // 生成破损退货单
            this.autoCreateDamageReturnOrder(orderDo);
        }

        return true;
    }

    /**
     * 如果反审，所有的明细的数量都为0，需要取消订单
     * @param order
     */
    private void cancelOrder(OrderDTO order) {

        // 清除发货数据，以及明细中的金额
        this.clearOrderDeliveryData(order.getId());

        // 清除退货单数据
        orderReturnService.deleteReturnOrder(order.getId());

        // 后台自动取消订单
        orderService.cancel(order.getId(),0l,false);
    }

    /**
     * 自动创建供应商退货单
     *
     * @param orderDTO
     */
    private void autoCreateSupplyerApplyReturn(OrderDTO orderDTO) {

        List<OrderDetailChangeDO> orderDetailChangeDTOList = orderDetailChangeService.listByOrderId(orderDTO.getId());

        List<OrderDetailRequest> orderDetailList = new ArrayList<>();

        orderDetailChangeDTOList = orderDetailChangeDTOList.stream()
            .filter(orderDetailChangeDTO -> orderDetailChangeDTO.getSellerReturnQuantity() > 0).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(orderDetailChangeDTOList)) {

            log.info("供应商发货数量已经充足，不需要生成退货单");
            return;
        }

        for (OrderDetailChangeDO orderDetailChangeDTO : orderDetailChangeDTOList) {

            OrderDetailRequest orderDetailRequest = new OrderDetailRequest();
            orderDetailRequest.setDetailId(orderDetailChangeDTO.getDetailId());
            orderDetailRequest.setGoodsId(orderDetailChangeDTO.getGoodsId());
            List<OrderDeliveryRequest> orderDeliveryList = new ArrayList<>();
            OrderDeliveryRequest orderDeliveryRequest = new OrderDeliveryRequest();
            orderDeliveryRequest.setBatchNo("");
            orderDeliveryRequest.setReturnQuantity(orderDetailChangeDTO.getSellerReturnQuantity());
            orderDeliveryList.add(orderDeliveryRequest);
            orderDetailRequest.setOrderDeliveryList(orderDeliveryList);
            orderDetailList.add(orderDetailRequest);
        }
        OrderReturnApplyRequest orderReturnApplyRequest = new OrderReturnApplyRequest();
        orderReturnApplyRequest.setOrderId(orderDTO.getId());
        orderReturnApplyRequest.setReturnType(OrderReturnTypeEnum.SELLER_RETURN_ORDER.getCode());
        orderReturnApplyRequest.setRemark("");
        orderReturnApplyRequest.setOrderDetailList(orderDetailList);

        OrderReturnApplyRequest request = orderReturnService.operateOrderReturnRequest(orderReturnApplyRequest);
        popReturnService.supplierApplyOrderReturn(request, orderDTO);
    }

    /**
     * 自动创建破损退货单
     *
     * @param orderDTO
     */
    private void autoCreateDamageReturnOrder(OrderDTO orderDTO) {

        List<OrderDeliveryDO> orderDeliveryDOlist = orderDeliveryService.getOrderDeliveryList(orderDTO.getId());
        orderDeliveryDOlist = orderDeliveryDOlist.stream().filter(e -> e.getDeliveryQuantity().compareTo(e.getReceiveQuantity()) == 1)
            .collect(Collectors.toList());

        if (CollectionUtil.isEmpty(orderDeliveryDOlist)) {

            log.info("全部都已经收货，无需产生破损退货单");

            return;
        }

        Map<Long, List<OrderDeliveryDO>> orderDeliveryDoMaps = orderDeliveryDOlist.stream()
            .collect(Collectors.groupingBy(OrderDeliveryDO::getDetailId));
        List<OrderDetailDTO> orderDetailDTOList = orderDetailService.getOrderDetailInfo(orderDTO.getId());
        List<OrderDetailRequest> orderDetailList = new ArrayList<>();

        for (OrderDetailDTO orderDetailDto : orderDetailDTOList) {

            OrderDetailRequest orderDetailRequest = new OrderDetailRequest();
            orderDetailRequest.setDetailId(orderDetailDto.getId());
            orderDetailRequest.setGoodsId(orderDetailDto.getGoodsId());

            List<OrderDeliveryDO> orderDeliveryList = orderDeliveryDoMaps.get(orderDetailDto.getId());

            if (CollectionUtil.isEmpty(orderDeliveryDOlist)) {

                continue;
            }

            List<OrderDeliveryRequest> orderDeliveryRequestList = new ArrayList<>();
            for (OrderDeliveryDO orderDeliveryDTO : orderDeliveryList) {

                OrderDeliveryRequest orderDeliveryRequest = new OrderDeliveryRequest();
                orderDeliveryRequest.setBatchNo(orderDeliveryDTO.getBatchNo());
                orderDeliveryRequest.setReturnQuantity(orderDeliveryDTO.getDeliveryQuantity() - orderDeliveryDTO.getReceiveQuantity());
                orderDeliveryRequestList.add(orderDeliveryRequest);
            }
            orderDetailRequest.setOrderDeliveryList(orderDeliveryRequestList);
            orderDetailList.add(orderDetailRequest);
        }

        if (CollectionUtil.isNotEmpty(orderDetailList)) {
            OrderReturnApplyRequest orderReturnApplyRequest = new OrderReturnApplyRequest();
            orderReturnApplyRequest.setOrderId(orderDTO.getId());
            orderReturnApplyRequest.setReturnType(OrderReturnTypeEnum.DAMAGE_RETURN_ORDER.getCode());
            orderReturnApplyRequest.setRemark("");
            orderReturnApplyRequest.setOrderDetailList(orderDetailList);

            OrderReturnApplyRequest request = orderReturnService.operateOrderReturnRequest(orderReturnApplyRequest);
            popReturnService.damageOrderReturn(request, orderDTO);
        }
    }


    /**
     * 校验反审数据
     * @param updateOrderNotAuditRequest
     * @param orderDTO
     * @return
     */
    private Map<Long, OrderDetailDO> validateOrderStatus_v2(ModifyOrderNotAuditRequest updateOrderNotAuditRequest, OrderDTO orderDTO) {
        List<Integer> allowOrderNotAuditStatus = Lists.newArrayList();
        allowOrderNotAuditStatus.add(OrderStatusEnum.DELIVERED.getCode());
        allowOrderNotAuditStatus.add(OrderStatusEnum.PARTDELIVERED.getCode());
        // 校验，反审核的订单的状态是否在已发货或者部分发货状态
        if (!allowOrderNotAuditStatus.contains(orderDTO.getOrderStatus())) {
            throw new BusinessException(OrderErrorCode.ORDER_NOT_AUDIT_STATUS_INCORRECT);
        }
        // 校验是是否为开票状态，否则提示无法反审核
        /*if (!ListUtil.toList(OrderInvoiceApplyStatusEnum.INVALID.getCode(),OrderInvoiceApplyStatusEnum.PENDING_APPLY.getCode(),OrderInvoiceApplyStatusEnum.DEFAULT_VALUE_APPLY.getCode()).contains(orderDTO.getInvoiceStatus())) {

            throw new BusinessException(OrderErrorCode.ORDER_NOT_AUDIT_INVOICE_STATUS_INCORRECT);
        }*/
        List<OrderInvoiceApplyDTO> orderInvoiceApplyList = orderInvoiceApplyService.listByOrderId(orderDTO.getId());
        if(CollectionUtil.isNotEmpty(orderInvoiceApplyList)){
            throw new BusinessException(OrderErrorCode.ORDER_NOT_AUDIT_INVOICE_STATUS_INCORRECT);
        }

        if (StringUtils.isBlank(updateOrderNotAuditRequest.getErpDeliveryNo()) || StringUtils.isBlank(updateOrderNotAuditRequest.getBatchNo())) {

            throw new BusinessException(OrderErrorCode.ORDER_INFO_PARAM_ERROR);
        }
        // 校验发货数量是否和商品购买数量一致
        List<OrderDetailDO> orderDetailList = orderDetailService.getOrderDetailByOrderIds(Collections.singletonList(updateOrderNotAuditRequest.getOrderId()));
        if (CollectionUtil.isEmpty(orderDetailList)) {
            throw new BusinessException(OrderErrorCode.ORDER_INFO_PARAM_ERROR);
        }
        return orderDetailList.stream().collect(Collectors.toMap(OrderDetailDO::getId, one -> one, (k1, k2) -> k1));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "订单反审核", businessType = BusinessTypeEnum.UPDATE, save = true)
    public Result<OrderModifyAuditChangeBO> modifyOrderNotAudit_v2(ModifyOrderNotAuditRequest orderNotAuditRequest) {
        log.info(".modifyOrderNotAudit:{}", JSON.toJSON(orderNotAuditRequest));

        OrderDTO orderDTO = orderService.getOrderInfo(orderNotAuditRequest.getOrderId());
        //是否需要退回账期
        Boolean isReducePaymentAccount = PaymentMethodEnum.PAYMENT_DAYS.getCode().equals(orderDTO.getPaymentMethod().longValue());
        // 校验反审核订单状态
        Map<Long, OrderDetailDO> detailDOMap = this.validateOrderStatus_v2(orderNotAuditRequest, orderDTO);
        //获取锁ID
        String lockId = "";
        //锁名称
        String lockName = this.getLockName(orderDTO.getOrderNo());
        OrderModifyAuditChangeBO changeBO = new OrderModifyAuditChangeBO();

        try {
            lockId = redisDistributedLock.lock(lockName, 60, 120, TimeUnit.SECONDS);
            if (StringUtils.isEmpty(lockId)) {

                throw new BusinessException(OrderErrorCode.ORDER_NOT_AUDIT_LOCK_INCORRECT, "系统繁忙，获取锁失败，orderNo:" + orderDTO.getOrderNo());
            }
            // 查询反审前的发货单
            List<OrderDeliveryDO> beforeDeliverys = orderDeliveryService.getOrderDeliveryList(orderNotAuditRequest.getOrderId());
            if (CollectionUtil.isEmpty(beforeDeliverys)) {

                throw new BusinessException(OrderErrorCode.ORDER_INFO_PARAM_ERROR);
            }
            // 订单ERP数据
            List<OrderDeliveryErpDO> beforeOrderDeliveryErpDOList = orderDeliveryErpService.listByOrderIds(ListUtil.toList(orderNotAuditRequest.getOrderId()));
            if (CollectionUtil.isEmpty(beforeOrderDeliveryErpDOList)) {
                throw new BusinessException(OrderErrorCode.ORDER_INFO_PARAM_ERROR);
            }
            List<OrderDeliveryErpDO> orderDeliveryErpDOList = beforeOrderDeliveryErpDOList.stream().filter(t -> orderNotAuditRequest.getErpDeliveryNo().equals(t.getErpDeliveryNo()) && orderNotAuditRequest.getBatchNo().equals(t.getBatchNo())).collect(Collectors.toList());
            if (CollectionUtil.isEmpty(orderDeliveryErpDOList)) {
                throw new BusinessException(OrderErrorCode.ORDER_INFO_PARAM_ERROR);
            }
            // 修改前的ERP批次发货数量
            Map<String, Integer> beforeErpQtyMap = orderDeliveryErpDOList.stream().collect(Collectors.groupingBy(t -> t.getDetailId() + Constants.SEPARATOR_MIDDLELINE + t.getBatchNo(),Collectors.summingInt(OrderDeliveryErpDO::getDeliveryQuantity)));
            //ERP发货批次关键字
            Set<String> deliveryErpBatchkeys = orderDeliveryErpDOList.stream().map(t -> t.getDetailId() + Constants.SEPARATOR_MIDDLELINE  + t.getBatchNo()).collect(Collectors.toSet());;
            // erp发货明细
            Set<Long> orderDetailIdList = orderDeliveryErpDOList.stream().map(t -> t.getDetailId()).collect(Collectors.toSet());;
            //订单发货数据
            Map<Long, Integer> beforeDelivery = beforeDeliverys.stream().collect(Collectors.groupingBy(OrderDeliveryDO::getDetailId, Collectors.summingInt(OrderDeliveryDO::getDeliveryQuantity)));
            //当前发货数量
            Map<Long, Integer> currentDelivery = Maps.newHashMap();
            changeBO.setIsReducePaymentAccount(false);
            // 查询退款总金额
            changeBO.setReturnTotalAmount(selectReturnAmount(orderNotAuditRequest.getOrderId()));
            // 表示存在退货需要更新退货金额以及清理退货数据
            if (CompareUtil.compare(changeBO.getReturnTotalAmount(),BigDecimal.ZERO) > 0) {
                // 表示是否需要退还账期
                changeBO.setIsReducePaymentAccount(isReducePaymentAccount);
            }
            // 退还冻结数量
            Map<Long,Integer> frozenMap = Maps.newHashMap();
            List<OrderReturnDTO>  orderReturnDTOList = orderReturnService.listPassedByOrderId(orderNotAuditRequest.getOrderId());
            if (CollectionUtil.isNotEmpty(orderReturnDTOList)) {
                List<OrderReturnDetailDO> orderReturnDetailDOList = orderReturnDetailService.getOrderReturnDetailByReturnIds(orderReturnDTOList.stream().map(t -> t.getId()).collect(Collectors.toList()));
                log.info("...getOrderReturnDetailByReturnIds..",JSON.toJSON(orderReturnDetailDOList));
                if (CollectionUtil.isNotEmpty(orderReturnDetailDOList)) {
                    frozenMap =  orderReturnDetailDOList.stream().collect(Collectors.toMap(OrderReturnDetailDO::getGoodsSkuId,OrderReturnDetailDO::getReturnQuantity));
                }
                // 清除退货单数据
                orderReturnService.deleteReturnOrder(orderNotAuditRequest.getOrderId());
                orderDetailChangeService.clearReturnDataByOrderId(orderNotAuditRequest.getOrderId());
            }


            orderDeliveryErpService.deleteDeliveryErpByErpOrderNo(orderNotAuditRequest.getErpDeliveryNo(),orderNotAuditRequest.getBatchNo());
            // 如果是一个出库单号，需要删除应收单数据
            List<OrderDeliveryErpDO> deliveryErpDOList = beforeOrderDeliveryErpDOList.stream().filter(t -> orderNotAuditRequest.getErpDeliveryNo().equals(t.getErpDeliveryNo())).collect(Collectors.toList());
            if (deliveryErpDOList.size() == orderDeliveryErpDOList.size()) {
                orderDeliveryReceivableService.deleteDeliveryReceivableByOrderNo(orderNotAuditRequest.getErpDeliveryNo());
            }
            // 本次变更的发货单数据
            List<OrderDeliveryDO> changeDeliveryList = beforeDeliverys.stream().filter(t -> deliveryErpBatchkeys.contains(t.getDetailId() + Constants.SEPARATOR_MIDDLELINE  + t.getBatchNo())).collect(Collectors.toList());
            List<Long> deliveryIdList = changeDeliveryList.stream().map(t -> t.getId()).collect(Collectors.toList());

            List<OrderDeliveryDO> updateDeliveryList = Lists.newArrayList();
            for (OrderDeliveryDO orderDeliveryDO : changeDeliveryList) {
                // 当前收货数量
                Integer erpQty = beforeErpQtyMap.get(orderDeliveryDO.getDetailId() + Constants.SEPARATOR_MIDDLELINE + orderDeliveryDO.getBatchNo());
                Integer deliveryQuantity = orderDeliveryDO.getDeliveryQuantity() - erpQty;
                if (CompareUtil.compare(deliveryQuantity,0) <= 0) {
                    continue;
                }
                OrderDeliveryDO updateDeliveryDo = PojoUtils.map(orderDeliveryDO,OrderDeliveryDO.class);
                updateDeliveryDo.setId(null);
                updateDeliveryDo.setDeliveryQuantity(orderDeliveryDO.getDeliveryQuantity() - erpQty);
                updateDeliveryDo.setReceiveQuantity(0);
                updateDeliveryList.add(updateDeliveryDo);
            }
            // 重新生成发货数据
            if (CollectionUtil.isNotEmpty(changeDeliveryList)) {
                orderDeliveryService.deleteOrderDeliveryByPrimaryKey(deliveryIdList,0l);
            }
            if (CollectionUtil.isNotEmpty(updateDeliveryList)) {

                orderDeliveryService.saveBatch(updateDeliveryList);
            }

            // 修改反审核状态以及反审核时间
            OrderDO orderDO = new OrderDO();
            orderDO.setId(orderDTO.getId());
            orderDO.setAuditModifyFlag(1);
            orderDO.setAuditModifyTime(new Date());

            List<OrderDeliveryDO> afterDeliverys = orderDeliveryService.getOrderDeliveryList(orderNotAuditRequest.getOrderId());
            if (CollectionUtil.isNotEmpty(afterDeliverys)) {
                afterDeliverys =  afterDeliverys.stream().filter(t -> orderDetailIdList.contains(t.getDetailId())).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(afterDeliverys)) {
                    currentDelivery  =  afterDeliverys.stream().collect(Collectors.groupingBy(t -> t.getDetailId(),Collectors.summingInt(OrderDeliveryDO::getDeliveryQuantity)));
                }
                orderDO.setOrderStatus(OrderStatusEnum.PARTDELIVERED.getCode());
            }else{
                orderDO.setOrderStatus(OrderStatusEnum.UNDELIVERED.getCode());
            }

            orderService.updateById(orderDO);
            // 库存记录变化记录
            Map<Long, Integer> inventoryMap = Maps.newHashMap();
            for (Long detailId : orderDetailIdList) {
                OrderDetailDO orderDetailDO = detailDOMap.get(detailId);
                // 当前发货数量
                Integer currentDeliveryQty = Optional.ofNullable(currentDelivery.get(detailId)).orElse(0);
                // 记录发货金额
                orderDetailChangeService.updateDeliveryInfo(detailId, currentDeliveryQty);
                //修改前发货数量
                Integer beforeDeliveryQty = Optional.ofNullable(beforeDelivery.get(detailId)).orElse(0);
                if (beforeDeliveryQty.equals(currentDeliveryQty)) {
                    continue;
                }
                // 记录库存变动结果，便于调用方处理库存释放逻辑
                inventoryMap.put(orderDetailDO.getGoodsSkuId(), beforeDeliveryQty - currentDeliveryQty);
                Integer frozenQty = frozenMap.get(orderDetailDO.getGoodsSkuId());
                if (frozenQty != null) {
                    frozenMap.put(orderDetailDO.getGoodsSkuId(),beforeDeliveryQty - currentDeliveryQty + frozenQty);
                } else {
                    frozenMap.put(orderDetailDO.getGoodsSkuId(),beforeDeliveryQty - currentDeliveryQty);
                }
            }
            changeBO.setInventoryChangeMap(inventoryMap);
            changeBO.setFrozenChangeMap(frozenMap);
        } catch (InterruptedException e) {

            return Result.failed("系统繁忙，获取锁失败，orderNo:" + orderDTO.getOrderNo());

        } finally {
            // 记录日志
            orderLogService.add(orderDTO.getId(), OrderLogTypeEnum.ORDER_ORDINARY_LOG.getCode(), "ERP提交反审请求");

            redisDistributedLock.releaseLock(lockName, lockId);
        }
        return Result.success(changeBO);
    }

    /**
     * 查询退款金额
     * @param orderId 订单ID
     * @return
     */
    private BigDecimal selectReturnAmount(Long orderId) {
        List<OrderDetailChangeDO> orderDetailChangeDOList = orderDetailChangeService.listByOrderId(orderId);
        BigDecimal sellerTotalReturnAmount = orderDetailChangeDOList.stream().map(OrderDetailChangeDO::getSellerReturnAmount).reduce(BigDecimal::add).get();
        // 破损退货金额
        BigDecimal totalReturnAmount = orderDetailChangeDOList.stream().map(OrderDetailChangeDO::getReturnAmount).reduce(BigDecimal::add).get();
        BigDecimal returnTotalAmount = totalReturnAmount.add(sellerTotalReturnAmount);
        BigDecimal returnCashDiscountAmount = orderDetailChangeDOList.stream().map(OrderDetailChangeDO::getSellerReturnCashDiscountAmount).reduce(BigDecimal::add).get();
        returnTotalAmount = returnTotalAmount.subtract(returnCashDiscountAmount);
        return returnTotalAmount;
    }
}

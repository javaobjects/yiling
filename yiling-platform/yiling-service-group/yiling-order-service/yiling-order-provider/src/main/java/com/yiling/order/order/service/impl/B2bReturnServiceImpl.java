package com.yiling.order.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.bo.OrderDetailChangeBO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderReturnDTO;
import com.yiling.order.order.dto.request.OrderDeliveryRequest;
import com.yiling.order.order.dto.request.OrderDetailRequest;
import com.yiling.order.order.dto.request.OrderReturnApplyRequest;
import com.yiling.order.order.dto.request.OrderReturnVerifyRequest;
import com.yiling.order.order.entity.OrderDO;
import com.yiling.order.order.entity.OrderDeliveryDO;
import com.yiling.order.order.entity.OrderDeliveryErpDO;
import com.yiling.order.order.entity.OrderDetailChangeDO;
import com.yiling.order.order.entity.OrderReturnDO;
import com.yiling.order.order.entity.OrderReturnDetailBatchDO;
import com.yiling.order.order.entity.OrderReturnDetailDO;
import com.yiling.order.order.entity.OrderReturnDetailErpDO;
import com.yiling.order.order.enums.OrderErpPushStatusEnum;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderReturnStatusEnum;
import com.yiling.order.order.enums.OrderReturnTypeEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.service.NoService;
import com.yiling.order.order.service.OrderDeliveryErpService;
import com.yiling.order.order.service.OrderDeliveryService;
import com.yiling.order.order.service.OrderDetailChangeService;
import com.yiling.order.order.service.OrderDetailService;
import com.yiling.order.order.service.OrderLogService;
import com.yiling.order.order.service.OrderReturnDetailBatchService;
import com.yiling.order.order.service.OrderReturnDetailErpService;
import com.yiling.order.order.service.OrderReturnDetailService;
import com.yiling.order.order.service.OrderReturnService;
import com.yiling.order.order.service.OrderService;
import com.yiling.order.order.service.ReturnService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.yiling.order.order.enums.NoEnum.ORDER_RETURN_NO;

/**
 * B2B退货单主流程代码
 *
 * @author: yong.zhang
 * @date: 2022/3/10
 */
@Slf4j
@Service("b2bReturnService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class B2bReturnServiceImpl implements ReturnService {

    private final OrderReturnService orderReturnService;

    private final OrderReturnDetailService orderReturnDetailService;

    private final OrderReturnDetailBatchService orderReturnDetailBatchService;

    private final OrderDeliveryErpService orderDeliveryErpService;

    private final OrderReturnDetailErpService orderReturnDetailErpService;

    private final OrderService orderService;

    private final OrderDetailService orderDetailService;

    private final OrderDeliveryService orderDeliveryService;

    private final OrderDetailChangeService orderDetailChangeService;

    private final OrderLogService orderLogService;

    private final NoService noService;

    @Override
    public OrderReturnDTO supplierApplyOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest, OrderDTO orderDTO) {
        log.info("createOrderReturn supplierApplyOrderReturn start, request:[{}]", JSONUtil.toJsonStr(orderReturnApplyRequest));
        OrderReturnTypeEnum orderReturnTypeEnum = OrderReturnTypeEnum.SELLER_RETURN_ORDER;
        OrderReturnDO orderReturnDO = saveApplyReturnOrder(orderReturnApplyRequest, orderReturnTypeEnum);

        //添加退货日志
        orderLogService.add(orderReturnDO.getId(), 2, "供应商发货退货单申请");
        return PojoUtils.map(orderReturnDO, OrderReturnDTO.class);
    }

    @Override
    public Boolean beforeDamageOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest) {
        return null;
    }

    @Override
    public Boolean damageOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest, OrderDTO orderDTO) {
        return null;
    }

    @Override
    public Boolean purchaseApplyReturnOrder(OrderReturnApplyRequest orderReturnApplyRequest, OrderDTO orderDTO) {
        log.info("createOrderReturn purchaseApplyReturnOrder start, request:[{}]", JSONUtil.toJsonStr(orderReturnApplyRequest));
        OrderReturnTypeEnum orderReturnTypeEnum = OrderReturnTypeEnum.BUYER_RETURN_ORDER;
        OrderReturnDO orderReturnDO = saveApplyReturnOrder(orderReturnApplyRequest, orderReturnTypeEnum);

        //添加退货日志
        orderLogService.add(orderReturnDO.getId(), 2, "采购商退货单申请");
        return true;
    }

    @Override
    public OrderReturnDTO verifyOrderReturn(OrderReturnVerifyRequest request) {
        log.info("verifyOrderReturn start :[{}]", request);
        OrderReturnDO orderReturnDO = orderReturnService.getById(request.getReturnId());
        // 审核的退货单必须是待审核阶段
        if (!orderReturnDO.getReturnStatus().equals(OrderReturnStatusEnum.ORDER_RETURN_PENDING.getCode())) {
            throw new BusinessException(OrderErrorCode.ORDER_RETURN_STATUS_ERROR);
        }
        // 审核通过
        OrderReturnDO orderReturn = new OrderReturnDO();
        orderReturn.setId(orderReturnDO.getId());
        orderReturn.setReturnAuditUser(request.getOpUserId());
        orderReturn.setReturnAuditTime(request.getOpTime());
        orderReturn.setOpUserId(request.getOpUserId());
        orderReturn.setOpTime(request.getOpTime());
        if (0 == request.getIsSuccess()) {
            // 退货单状态修改
            orderReturn.setReturnStatus(OrderReturnStatusEnum.ORDER_RETURN_PASS.getCode());
            orderReturnService.updateById(orderReturn);

            // 修改订单明细修改表的数据(退货金额)
            updateOrderReturnDetailAmount(orderReturnDO.getId());

            // 修改订单里面的退款数据
            updateOrderReturnAmount(orderReturnDO.getOrderId());

            // 日志
            orderLogService.add(orderReturnDO.getId(), 2, "审核通过退货单");
        } else {
            // 审核驳回
            orderReturn.setReturnStatus(OrderReturnStatusEnum.ORDER_RETURN_REJECT.getCode());
            orderReturn.setFailReason(request.getFailReason());
            orderReturnService.updateById(orderReturn);

            // 日志
            orderLogService.add(orderReturnDO.getId(), 2, "审核驳回退货单");
        }

        return PojoUtils.map(orderReturnDO, OrderReturnDTO.class);
    }

    /**
     * 修改订单明细修改表的数据(退货金额)
     *
     * @param returnId 退货单id
     */
    private void updateOrderReturnDetailAmount(Long returnId) {
        List<OrderReturnDetailDO> returnDetailDOList = orderReturnDetailService.getOrderReturnDetailByReturnId(returnId);
        for (OrderReturnDetailDO orderReturnDetailDO : returnDetailDOList) {
            OrderDetailChangeBO orderDetailChangeBO = orderDetailChangeService.updateReturnData(orderReturnDetailDO.getDetailId(), orderReturnDetailDO.getReturnQuantity(), true);
            log.info("verifyOrderReturn updateOrderReturnDetailAmount, orderDetailChangeBO :[{}],orderReturnDetailDO:[{}]", orderDetailChangeBO, orderReturnDetailDO);
        }
    }

    /**
     * 判断订单是否全部退货
     *
     * @param orderId 订单id
     * @return 是/否
     */
    private boolean isAllReturn(Long orderId) {
        List<OrderDetailChangeDO> orderDetailChangeDOList = orderDetailChangeService.listByOrderId(orderId);
        for (OrderDetailChangeDO orderDetailChangeDO : orderDetailChangeDOList) {
            if (orderDetailChangeDO.getGoodsQuantity() > orderDetailChangeDO.getReturnQuantity() + orderDetailChangeDO.getSellerReturnQuantity()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 订单表修改退货总金额
     *
     * @param orderId 订单id
     */
    private void updateOrderReturnAmount(Long orderId) {
        // 计算出订单的所有退货单的总退款相关金额
        List<OrderReturnDTO> orderReturnDTOList = orderReturnService.listPassedByOrderId(orderId);
        BigDecimal returnAmount = BigDecimal.ZERO;
        BigDecimal returnPlatformCouponDiscountAmount = BigDecimal.ZERO;
        BigDecimal returnCouponDiscountAmount = BigDecimal.ZERO;
        BigDecimal returnPresaleDiscountAmount = BigDecimal.ZERO;
        BigDecimal returnPlatformPaymentDiscountAmount = BigDecimal.ZERO;
        BigDecimal returnShopPaymentDiscountAmount = BigDecimal.ZERO;
        for (OrderReturnDTO orderReturnDTO : orderReturnDTOList) {
            returnAmount = returnAmount.add(orderReturnDTO.getReturnAmount());
            returnPlatformCouponDiscountAmount = returnPlatformCouponDiscountAmount.add(orderReturnDTO.getPlatformCouponDiscountAmount());
            returnCouponDiscountAmount = returnCouponDiscountAmount.add(orderReturnDTO.getCouponDiscountAmount());
            returnPresaleDiscountAmount = returnPresaleDiscountAmount.add(orderReturnDTO.getPresaleDiscountAmount());
            returnPlatformPaymentDiscountAmount = returnPlatformPaymentDiscountAmount.add(orderReturnDTO.getPlatformPaymentDiscountAmount());
            returnShopPaymentDiscountAmount = returnShopPaymentDiscountAmount.add(orderReturnDTO.getShopPaymentDiscountAmount());
        }

        // 订单表退款金额相关数据修改
        OrderDO orderDO = new OrderDO();
        orderDO.setId(orderId);
        orderDO.setReturnAmount(returnAmount);
        orderDO.setReturnPlatformCouponDiscountAmount(returnPlatformCouponDiscountAmount);
        orderDO.setReturnCouponDiscountAmount(returnCouponDiscountAmount);
        orderDO.setReturnPresaleDiscountAmount(returnPresaleDiscountAmount);
        orderDO.setReturnPlatformPaymentDiscountAmount(returnPlatformPaymentDiscountAmount);
        orderDO.setReturnShopPaymentDiscountAmount(returnShopPaymentDiscountAmount);
        if (isAllReturn(orderId)) {
            // 订单全部退货时，订单状态改为已完成
            orderDO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
            orderDO.setFinishTime(new Date());
        }
        log.info("updateOrderReturnAmount, orderDO :[{}]", JSONUtil.toJsonStr(orderDO));
        orderService.updateById(orderDO);
    }

    /**
     * 退货单申请主逻辑
     *
     * @param request 请求数据
     * @return 退货单数据
     */
    private OrderReturnDO saveApplyReturnOrder(OrderReturnApplyRequest request, OrderReturnTypeEnum orderReturnTypeEnum) {
        List<OrderDetailRequest> orderDetailList = request.getOrderDetailList();
        OrderDTO orderDTO = orderService.getOrderInfo(request.getOrderId());
        List<OrderDetailDTO> orderDetailDTOList = orderDetailService.getOrderDetailInfo(orderDTO.getId());
        Map<Long, OrderDetailDTO> orderDetailMap = orderDetailDTOList.stream().collect(Collectors.toMap(OrderDetailDTO::getId, o -> o, (k1, k2) -> k1));
        List<OrderDeliveryDO> orderDeliveryDTOList = orderDeliveryService.getOrderDeliveryList(orderDTO.getId());
        Map<String, OrderDeliveryDO> orderDeliveryMap = orderDeliveryDTOList.stream().collect(Collectors.toMap(k -> k.getDetailId() + "-" + k.getBatchNo(), o -> o, (k1, k2) -> k1));

        // 申请退货单数据校验
        validApplyOrderReturn(orderReturnTypeEnum, orderDTO, orderDetailList, orderDeliveryMap);

        log.info("createOrderReturn saveApplyReturnOrder start !");
        List<OrderReturnDetailBatchDO> orderReturnDetailBatchDOList = new ArrayList<>();
        List<OrderReturnDetailErpDO> orderReturnDetailErpDOList = new ArrayList<>();
        List<OrderReturnDetailDO> orderReturnDetailDOList = new ArrayList<>();
        BigDecimal totalReturnAmount = BigDecimal.ZERO;
        BigDecimal totalPlatformCouponDiscountAmount = BigDecimal.ZERO;
        BigDecimal totalCouponDiscountAmount = BigDecimal.ZERO;
        BigDecimal totalPresaleDiscountAmount =  BigDecimal.ZERO;
        BigDecimal totalPlatformPaymentDiscountAmount = BigDecimal.ZERO;
        BigDecimal totalShopPaymentDiscountAmount = BigDecimal.ZERO;

        for (OrderDetailRequest orderDetailRequest : orderDetailList) {
            int returnQuantity = 0;
            boolean isHaveOrderDetailBatch = false;
            List<OrderDeliveryRequest> orderDeliveryList = orderDetailRequest.getOrderDeliveryList();
            for (OrderDeliveryRequest orderDeliveryRequest : orderDeliveryList) {
                if (0 == orderDeliveryRequest.getReturnQuantity()) {
                    continue;
                }
                isHaveOrderDetailBatch = true;
                OrderReturnDetailBatchDO orderReturnDetailBatchDO = saveOrderReturnDetailBatch(orderDetailRequest.getDetailId(), orderDeliveryRequest.getBatchNo(), orderDeliveryRequest.getReturnQuantity());
                if (StringUtils.isNotBlank(orderDeliveryRequest.getBatchNo())) {
                    // 采购商退货，批次号为空
                    OrderDeliveryDO orderDeliveryDO = orderDeliveryMap.get(orderDetailRequest.getDetailId() + "-" + orderDeliveryRequest.getBatchNo());
                    orderReturnDetailBatchDO.setExpiryDate(orderDeliveryDO.getExpiryDate());
                }
                orderReturnDetailBatchDOList.add(orderReturnDetailBatchDO);

                // 退货单明细出库单表数据的添加
                List<OrderReturnDetailErpDO> orderReturnDetailErpList = saveOrderDeliveryErp(orderReturnDetailBatchDO);
                orderReturnDetailErpDOList.addAll(orderReturnDetailErpList);

                returnQuantity = returnQuantity + orderDeliveryRequest.getReturnQuantity();
            }

            if (!isHaveOrderDetailBatch) {
                // 如果这个退货单明细不存在退货单明细批次，则说明这个退货单明细不应该生成
                continue;
            }

            if (0 != returnQuantity) {
                OrderReturnDetailDO orderReturnDetailDO = saveOrderReturnDetail(orderReturnTypeEnum, orderDetailMap.get(orderDetailRequest.getDetailId()), returnQuantity);
                orderReturnDetailDOList.add(orderReturnDetailDO);

                totalReturnAmount = totalReturnAmount.add(orderReturnDetailDO.getReturnAmount());
                totalPlatformCouponDiscountAmount = totalPlatformCouponDiscountAmount.add(orderReturnDetailDO.getReturnPlatformCouponDiscountAmount());
                totalCouponDiscountAmount = totalCouponDiscountAmount.add(orderReturnDetailDO.getReturnCouponDiscountAmount());
                totalPresaleDiscountAmount = totalPresaleDiscountAmount.add(orderReturnDetailDO.getReturnPresaleDiscountAmount());
                totalPlatformPaymentDiscountAmount = totalPlatformPaymentDiscountAmount.add(orderReturnDetailDO.getReturnPlatformPaymentDiscountAmount());
                totalShopPaymentDiscountAmount = totalShopPaymentDiscountAmount.add(orderReturnDetailDO.getReturnShopPaymentDiscountAmount());
            }
        }

        // 退货单数据的添加
        OrderReturnDO orderReturnDO = saveOrderReturnDO(orderReturnTypeEnum, orderDTO, request.getRemark());
        orderReturnDO.setReturnAmount(totalReturnAmount);
        orderReturnDO.setPlatformCouponDiscountAmount(totalPlatformCouponDiscountAmount);
        orderReturnDO.setCouponDiscountAmount(totalCouponDiscountAmount);
        orderReturnDO.setPresaleDiscountAmount(totalPresaleDiscountAmount);
        orderReturnDO.setPlatformPaymentDiscountAmount(totalPlatformPaymentDiscountAmount);
        orderReturnDO.setShopPaymentDiscountAmount(totalShopPaymentDiscountAmount);
        orderReturnDO.setOpUserId(request.getOpUserId());
        orderReturnDO.setOpTime(request.getOpTime());
        orderReturnService.save(orderReturnDO);
        log.info("createOrderReturn saveApplyReturnOrder, orderReturnDO :[{}]", orderReturnDO);

        // 退货单明细数据的添加
        orderReturnDetailDOList.forEach(e -> {
            e.setReturnId(orderReturnDO.getId());
            e.setOpUserId(request.getOpUserId());
            e.setOpTime(request.getOpTime());
        });

        // 退货单明细批次数据的添加
        orderReturnDetailBatchDOList.forEach(e -> {
            e.setReturnId(orderReturnDO.getId());
            e.setOpUserId(request.getOpUserId());
            e.setOpTime(request.getOpTime());
        });

        // 退货单明细出库单表数据的添加
        orderReturnDetailErpDOList.forEach(e -> {
            e.setReturnId(orderReturnDO.getId());
            e.setOpUserId(request.getOpUserId());
            e.setOpTime(request.getOpTime());
        });

        // 修改订单里面的退款数据
        if (orderReturnTypeEnum.getCode().equals(OrderReturnTypeEnum.SELLER_RETURN_ORDER.getCode())) {
            updateOrderReturnAmount(orderDTO.getId());
        }

        log.info("createOrderReturn saveApplyReturnOrder, orderReturnDetailDOList :[{}]", orderReturnDetailDOList);
        orderReturnDetailService.saveBatch(orderReturnDetailDOList);

        log.info("createOrderReturn saveApplyReturnOrder, orderReturnDetailBatchDOList :[{}]", orderReturnDetailBatchDOList);
        orderReturnDetailBatchService.saveBatch(orderReturnDetailBatchDOList);

        log.info("createOrderReturn saveApplyReturnOrder, orderReturnDetailErpDOList :[{}]", orderReturnDetailErpDOList);
        orderReturnDetailErpService.saveBatch(orderReturnDetailErpDOList);
        return orderReturnDO;
    }

    /**
     * 退货单数据添加
     *
     * @param orderReturnTypeEnum 退货单类型
     * @param orderDTO 订单信息
     * @return 退货单数据
     */
    private OrderReturnDO saveOrderReturnDO(OrderReturnTypeEnum orderReturnTypeEnum, OrderDTO orderDTO, String remark) {
        // 退货单表信息存储
        OrderReturnDO orderReturnDO = new OrderReturnDO();
        String orderReturnId = noService.gen(ORDER_RETURN_NO);
        orderReturnDO.setOrderId(orderDTO.getId());
        orderReturnDO.setOrderNo(orderDTO.getOrderNo());
        orderReturnDO.setReturnNo(orderReturnId);
        orderReturnDO.setReturnSource(orderDTO.getOrderSource());
        orderReturnDO.setOrderReturnType(orderDTO.getOrderType());
        orderReturnDO.setBuyerEid(orderDTO.getBuyerEid());
        orderReturnDO.setBuyerEname(orderDTO.getBuyerEname());
        orderReturnDO.setSellerEid(orderDTO.getSellerEid());
        orderReturnDO.setSellerEname(orderDTO.getSellerEname());
        orderReturnDO.setDistributorEid(orderDTO.getDistributorEid());
        orderReturnDO.setDistributorEname(orderDTO.getDistributorEname());
        orderReturnDO.setReturnType(orderReturnTypeEnum.getCode());
        orderReturnDO.setReturnStatus(OrderReturnStatusEnum.ORDER_RETURN_PENDING.getCode());
        orderReturnDO.setContacterId(orderDTO.getContacterId());
        orderReturnDO.setContacterName(orderDTO.getContacterName());
        orderReturnDO.setErpPushStatus(OrderErpPushStatusEnum.NOT_PUSH.getCode());
        orderReturnDO.setRemark(remark);
        if (orderReturnTypeEnum.getCode().equals(OrderReturnTypeEnum.SELLER_RETURN_ORDER.getCode())) {
            orderReturnDO.setReturnStatus(OrderReturnStatusEnum.ORDER_RETURN_PASS.getCode());
            orderReturnDO.setReturnAuditTime(new Date());
        }
        return orderReturnDO;
    }

    /**
     * 退货单明细数据添加
     *
     * @param orderReturnTypeEnum 退货单类型
     * @param orderDetailDTO 订单明细信息
     * @param returnQuantity 退货数量
     * @return 退货单明细
     */
    private OrderReturnDetailDO saveOrderReturnDetail(OrderReturnTypeEnum orderReturnTypeEnum, OrderDetailDTO orderDetailDTO, int returnQuantity) {
        OrderReturnDetailDO orderReturnDetailDO = new OrderReturnDetailDO();
        orderReturnDetailDO.setDetailId(orderDetailDTO.getId()).setStandardId(orderDetailDTO.getStandardId()).setGoodsId(orderDetailDTO.getGoodsId()).setGoodsSkuId(orderDetailDTO.getGoodsSkuId()).setGoodsErpCode(orderDetailDTO.getGoodsErpCode()).setReturnQuantity(returnQuantity);
        if (orderReturnTypeEnum.getCode().equals(OrderReturnTypeEnum.BUYER_RETURN_ORDER.getCode())) {
            OrderDetailChangeBO orderDetailChangeBO = orderDetailChangeService.updateReturnData(orderDetailDTO.getId(), returnQuantity, false);
            orderReturnDetailDO.setReturnAmount(orderDetailChangeBO.getAmount());
            orderReturnDetailDO.setReturnPlatformCouponDiscountAmount(orderDetailChangeBO.getPlatformCouponDiscountAmount());
            orderReturnDetailDO.setReturnCouponDiscountAmount(orderDetailChangeBO.getCouponDiscountAmount());
            orderReturnDetailDO.setReturnPresaleDiscountAmount(orderDetailChangeBO.getPresaleDiscountAmount());
            orderReturnDetailDO.setReturnPlatformPaymentDiscountAmount(orderDetailChangeBO.getPlatformPaymentDiscountAmount());
            orderReturnDetailDO.setReturnShopPaymentDiscountAmount(orderDetailChangeBO.getShopPaymentDiscountAmount());
        } else if (orderReturnTypeEnum.getCode().equals(OrderReturnTypeEnum.SELLER_RETURN_ORDER.getCode())) {
            OrderDetailChangeDO orderDetailChangeDO = orderDetailChangeService.getByDetailId(orderDetailDTO.getId());
            orderReturnDetailDO.setReturnAmount(orderDetailChangeDO.getSellerReturnAmount());
            orderReturnDetailDO.setReturnPlatformCouponDiscountAmount(orderDetailChangeDO.getSellerPlatformCouponDiscountAmount());
            orderReturnDetailDO.setReturnCouponDiscountAmount(orderDetailChangeDO.getSellerCouponDiscountAmount());
            orderReturnDetailDO.setReturnPresaleDiscountAmount(orderDetailChangeDO.getSellerPresaleDiscountAmount());

            orderReturnDetailDO.setReturnPlatformPaymentDiscountAmount(orderDetailChangeDO.getSellerPlatformPaymentDiscountAmount());
            orderReturnDetailDO.setReturnShopPaymentDiscountAmount(orderDetailChangeDO.getSellerShopPaymentDiscountAmount());
        }
        return orderReturnDetailDO;
    }

    /**
     * 根据退货单明细批次信息修改erp出库单信息
     *
     * @param orderReturnDetailBatchDO 退货单明细批次信息
     * @return 退货单明细出库信息
     */
    private List<OrderReturnDetailErpDO> saveOrderDeliveryErp(OrderReturnDetailBatchDO orderReturnDetailBatchDO) {
        List<OrderReturnDetailErpDO> entityList = new ArrayList<>();
        if (StringUtils.isBlank(orderReturnDetailBatchDO.getBatchNo())) {
            return entityList;
        }
        List<OrderDeliveryErpDO> orderDeliveryErpDOList = orderDeliveryErpService.listByDetailIdAndBatchNo(null, orderReturnDetailBatchDO.getDetailId(), orderReturnDetailBatchDO.getBatchNo());
        if (CollUtil.isEmpty(orderDeliveryErpDOList)) {
            return entityList;
        }
        Integer returnQuantity = orderReturnDetailBatchDO.getReturnQuantity();

        log.info("saveApplyReturnOrder saveOrderDeliveryErp, returnQuantity to batch :[{}], orderDeliveryErpDOList :[{}]", returnQuantity, orderDeliveryErpDOList);
        // 批次退货数量分摊到各个出库单号上面
        for (OrderDeliveryErpDO orderDeliveryErpDO : orderDeliveryErpDOList) {
            Integer deliveryQuantity = orderDeliveryErpDO.getDeliveryQuantity();
            // 此出库单号已经退货数量
            Integer allReturnQuality = orderReturnDetailErpService.sumReturnQualityByErpDeliveryNo(orderDeliveryErpDO.getDetailId(), orderDeliveryErpDO.getBatchNo(), orderDeliveryErpDO.getErpDeliveryNo(),StringUtils.isNotBlank(orderDeliveryErpDO.getErpSendOrderId()) ? orderDeliveryErpDO.getErpSendOrderId() : null );
            // 剩余的可退数量
            int lastReturnQuality = deliveryQuantity - allReturnQuality;
            if (returnQuantity > 0 && lastReturnQuality > 0) {
                OrderReturnDetailErpDO orderReturnDetailErpDO = new OrderReturnDetailErpDO();
                orderReturnDetailErpDO.setReturnId(orderReturnDetailBatchDO.getReturnId());
                orderReturnDetailErpDO.setDetailId(orderDeliveryErpDO.getDetailId());
                orderReturnDetailErpDO.setBatchNo(orderDeliveryErpDO.getBatchNo());
                orderReturnDetailErpDO.setExpiryDate(orderReturnDetailBatchDO.getExpiryDate());
                orderReturnDetailErpDO.setErpDeliveryNo(orderDeliveryErpDO.getErpDeliveryNo());
                orderReturnDetailErpDO.setErpSendOrderId(orderDeliveryErpDO.getErpSendOrderId());
                if (lastReturnQuality >= returnQuantity) {
                    orderReturnDetailErpDO.setReturnQuantity(returnQuantity);
                    returnQuantity = 0;
                } else {
                    orderReturnDetailErpDO.setReturnQuantity(lastReturnQuality);
                    returnQuantity = returnQuantity - lastReturnQuality;
                }
                entityList.add(orderReturnDetailErpDO);
            }
        }
        return entityList;
    }

    /**
     * 退货单明细批次表数据添加
     *
     * @param detailId 订单明细id
     * @param batchNo 批次号
     * @param returnQuality 退货数量
     * @return 退货单明细批次信息
     */
    private OrderReturnDetailBatchDO saveOrderReturnDetailBatch(Long detailId, String batchNo, Integer returnQuality) {
        return new OrderReturnDetailBatchDO().setDetailId(detailId).setBatchNo(batchNo).setReturnQuantity(returnQuality);
    }

    /**
     * 申请退货单数据校验
     *
     * @param orderDTO 订单信息
     * @param orderDetailList 请求的退货单明细
     * @param orderDeliveryMap 订单出库信息
     */
    private void validApplyOrderReturn(OrderReturnTypeEnum orderReturnTypeEnum, OrderDTO orderDTO, List<OrderDetailRequest> orderDetailList, Map<String, OrderDeliveryDO> orderDeliveryMap) {
        log.info("createOrderReturn saveApplyReturnOrder, validApplyOrderReturn orderDTO :[{}], orderDetailList :[{}],orderDeliveryMap :[{}]", orderDTO, orderDetailList, orderDeliveryMap);
        // 退货单明细不能为空
        if (CollectionUtil.isEmpty(orderDetailList)) {
            log.info("申请的退货单没有选择商品");
            throw new BusinessException(OrderErrorCode.ORDER_RETURN_DETAIL_NOT_ERROR);
        }
        // 退货单明细批次不能为空
        for (OrderDetailRequest request : orderDetailList) {
            if (CollectionUtil.isEmpty(request.getOrderDeliveryList())) {
                log.info("createOrderReturn saveApplyReturnOrder,申请的退货单有商品没有明细批次信息");
                throw new BusinessException(OrderErrorCode.ORDER_RETURN_DETAIL_BATCH_NOT_ERROR);
            }
        }
        Date deliveryTime = orderDTO.getDeliveryTime();
        // 只有订单状态为已发货的允许退货
        if (!OrderStatusEnum.DELIVERED.getCode().equals(orderDTO.getOrderStatus())) {
            log.info("createOrderReturn saveApplyReturnOrder,订单未发货或已收货，不允许线上申请退货，请联系供应商线下退货");
            throw new BusinessException(OrderErrorCode.B2B_ORDER_RETURN_ORDER_STATUS_ERROR);
        }

        // 存在审核中的退货单不允许申请退货
        int count = orderReturnService.countByOrderIdAndStatus(orderDTO.getId(), OrderReturnStatusEnum.ORDER_RETURN_PENDING.getCode());
        if (count > 0) {
            log.info("createOrderReturn saveApplyReturnOrder,已提交的退货单正在审核，请联系供应商加快审核，审核完毕后可再次提交退货申请！");
            throw new BusinessException(OrderErrorCode.ORDER_RETURN_STATUS_IS_AUDIT_EXIST);
        }

        // 只有7天内允许退货
        Date addDays = DateUtils.addDays(deliveryTime, 7);
        if (addDays.getTime() < System.currentTimeMillis()) {
            log.info("createOrderReturn saveApplyReturnOrder,只有订单发货后7天内允许线上退货");
            throw new BusinessException(OrderErrorCode.B2B_ORDER_RETURN_ORDER_TIME_ERROR);
        }

        // 批次退货数量不能大于批次剩余
        // 供应商退货时，由于数据是自己拼接的，所以不需要校验数量
        if (orderReturnTypeEnum == OrderReturnTypeEnum.SELLER_RETURN_ORDER) {
            return;
        }
        for (OrderDetailRequest request : orderDetailList) {
            List<OrderDeliveryRequest> orderDeliveryList = request.getOrderDeliveryList();
            for (OrderDeliveryRequest orderDeliveryRequest : orderDeliveryList) {
                List<OrderReturnDetailBatchDO> orderReturnDetailBatchDOList = orderReturnDetailBatchService.queryByDetailIdAndBatchNoAndType(request.getDetailId(), orderDeliveryRequest.getBatchNo(), null);
                Integer returnQuality = orderReturnDetailBatchDOList.stream().mapToInt(OrderReturnDetailBatchDO::getReturnQuantity).sum();
                OrderDeliveryDO orderDeliveryDO = orderDeliveryMap.get(request.getDetailId() + "-" + orderDeliveryRequest.getBatchNo());
                if (null == orderDeliveryDO) {
                    log.info("createOrderReturn saveApplyReturnOrder,申请的退货单商品明细批次不存在");
                    throw new BusinessException(OrderErrorCode.ORDER_RETURN_DETAIL_BATCH_FALSE_ERROR);
                }
                if (returnQuality + orderDeliveryRequest.getReturnQuantity() > orderDeliveryDO.getDeliveryQuantity()) {
                    log.info("createOrderReturn saveApplyReturnOrder,批次号退货数量超出收货剩余数量");
                    throw new BusinessException(OrderErrorCode.ORDER_RETURN_QUANTITY_OVERSTEP);
                }
            }
        }
    }
}

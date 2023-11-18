package com.yiling.order.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.yiling.order.order.dto.request.RefundOrderDetailTicketDiscountRequest;
import com.yiling.order.order.dto.request.RefundOrderTicketDiscountRequest;
import com.yiling.order.order.entity.OrderDO;
import com.yiling.order.order.entity.OrderDeliveryDO;
import com.yiling.order.order.entity.OrderDeliveryErpDO;
import com.yiling.order.order.entity.OrderDetailChangeDO;
import com.yiling.order.order.entity.OrderDetailTicketDiscountDO;
import com.yiling.order.order.entity.OrderReturnDO;
import com.yiling.order.order.entity.OrderReturnDetailBatchDO;
import com.yiling.order.order.entity.OrderReturnDetailDO;
import com.yiling.order.order.entity.OrderReturnDetailErpDO;
import com.yiling.order.order.entity.OrderTicketDiscountDO;
import com.yiling.order.order.entity.TicketDiscountRecordDO;
import com.yiling.order.order.enums.OrderErpPushStatusEnum;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderInvoiceApplyStatusEnum;
import com.yiling.order.order.enums.OrderReturnInvoiceStatusEnum;
import com.yiling.order.order.enums.OrderReturnStatusEnum;
import com.yiling.order.order.enums.OrderReturnTypeEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.service.NoService;
import com.yiling.order.order.service.OrderDeliveryErpService;
import com.yiling.order.order.service.OrderDeliveryService;
import com.yiling.order.order.service.OrderDetailChangeService;
import com.yiling.order.order.service.OrderDetailService;
import com.yiling.order.order.service.OrderDetailTicketDiscountService;
import com.yiling.order.order.service.OrderLogService;
import com.yiling.order.order.service.OrderReturnDetailBatchService;
import com.yiling.order.order.service.OrderReturnDetailErpService;
import com.yiling.order.order.service.OrderReturnDetailService;
import com.yiling.order.order.service.OrderReturnService;
import com.yiling.order.order.service.OrderService;
import com.yiling.order.order.service.OrderTicketDiscountService;
import com.yiling.order.order.service.ReturnService;
import com.yiling.order.order.service.TicketDiscountRecordService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.yiling.order.order.enums.NoEnum.ORDER_RETURN_NO;

/**
 * POP退货单主逻辑服务
 *
 * @author: yong.zhang
 * @date: 2022/3/10
 */
@Slf4j
@Service("popReturnService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PopReturnServiceImpl implements ReturnService {

    private final OrderReturnService orderReturnService;

    private final OrderDetailService orderDetailService;

    private final OrderService orderService;

    private final OrderReturnDetailService orderReturnDetailService;

    private final OrderReturnDetailBatchService orderReturnDetailBatchService;

    private final OrderDeliveryService orderDeliveryService;

    private final OrderDetailChangeService orderDetailChangeService;

    private final OrderDetailTicketDiscountService orderDetailTicketDiscountService;

    private final TicketDiscountRecordService ticketDiscountRecordService;

    private final OrderLogService orderLogService;

    private final NoService noService;

    private final OrderTicketDiscountService orderTicketDiscountService;

    private final OrderDeliveryErpService orderDeliveryErpService;

    private final OrderReturnDetailErpService orderReturnDetailErpService;

    @Override
    public OrderReturnDTO supplierApplyOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest, OrderDTO orderDTO) {
        // 退货单申请:供应商退货
        log.info("createOrderReturn supplierApplyOrderReturn start, orderDTO :[{}]", orderDTO);

        // 退货单申请:供应商退货单
        OrderReturnDO orderReturnDO = saveApplyReturnOrder(orderReturnApplyRequest, orderDTO, OrderReturnTypeEnum.SELLER_RETURN_ORDER);

        //添加退货日志
        orderLogService.add(orderReturnDO.getId(), 2, "供应商提交退货单申请");
        return PojoUtils.map(orderReturnDO, OrderReturnDTO.class);
    }

    @Override
    public Boolean beforeDamageOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest) {
        // 商品明细
        List<OrderDetailRequest> orderDetailList = orderReturnApplyRequest.getOrderDetailList();
        for (OrderDetailRequest orderDetailRequest : orderDetailList) {
            int totalReturnQuality = 0;
            // 商品批次明细
            List<OrderDeliveryRequest> orderDeliveryList = orderDetailRequest.getOrderDeliveryList();
            for (OrderDeliveryRequest orderDeliveryRequest : orderDeliveryList) {
                saveOrderDelivery(orderReturnApplyRequest.getOrderId(), orderDetailRequest.getDetailId(), orderDeliveryRequest.getBatchNo(), orderDeliveryRequest.getReturnQuantity());
                totalReturnQuality = totalReturnQuality + orderDeliveryRequest.getReturnQuantity();
            }
            saveOrderDetailChange(orderDetailRequest.getDetailId(), totalReturnQuality);
        }
        return true;
    }

    @Override
    public Boolean damageOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest, OrderDTO orderDTO) {
        // 退货单申请:破损退货
        // 创建需要审核退货单(退货单和退货单详情信息)
        // 创建退货单条件：已经收货
        if (null == orderDTO) {
            Long orderId = orderReturnApplyRequest.getOrderId();
            //查询订单详情
            orderDTO = orderService.getOrderInfo(orderId);
        }
        log.info("createOrderReturn damageOrderReturn start, orderDTO :[{}]", orderDTO);

        // 退货单信息创建主逻辑
        OrderReturnDO orderReturnDO = saveApplyReturnOrder(orderReturnApplyRequest, orderDTO, OrderReturnTypeEnum.DAMAGE_RETURN_ORDER);
        //添加退货日志
        orderLogService.add(orderReturnDO.getId(), 2, "采购商提交退回退货单申请");
        return true;
    }

    @Override
    public Boolean purchaseApplyReturnOrder(OrderReturnApplyRequest orderReturnApplyRequest, OrderDTO orderDTO) {
        // 退货单申请:采购商退货
        // 创建需要审核退货单(退货单和退货单详情信息)
        // 创建退货单条件：1.已经收获，2.订单发票状态是否已经改为已开票
        log.info("createOrderReturn purchaseApplyReturnOrder start, orderDTO :[{}]", orderDTO);

        // 退货单信息创建主逻辑
        OrderReturnDO orderReturnDO = saveApplyReturnOrder(orderReturnApplyRequest, orderDTO, OrderReturnTypeEnum.BUYER_RETURN_ORDER);

        //添加退货日志
        orderLogService.add(orderReturnDO.getId(), 2, "采购商提交退货单申请");
        return true;
    }

    @Override
    public OrderReturnDTO verifyOrderReturn(OrderReturnVerifyRequest request) {
        if (null == request.getReturnId()) {
            throw new BusinessException(OrderErrorCode.ORDER_INFO_PARAM_ERROR);
        }
        OrderReturnDO orderReturnDO = orderReturnService.getById(request.getReturnId());
        checkVerifyRequest(orderReturnDO);
        if (1 == request.getIsSuccess()) {
            // 审核驳回
            rejectOrderReturn(request, orderReturnDO);
        } else {
            // 审核通过
            approvedOrderReturn(request, orderReturnDO);
        }
        return PojoUtils.map(orderReturnDO, OrderReturnDTO.class);
    }

    /**
     * 总体校验退货单申请和退货单数据
     *
     * @param orderReturnDO 退货单信息
     */
    private void checkVerifyRequest(OrderReturnDO orderReturnDO) {
        if (null == orderReturnDO) {
            throw new BusinessException(OrderErrorCode.ORDER_INFO_PARAM_ERROR);
        }
        // 只有待审核退货单可以进行驳回操作
        if (OrderReturnStatusEnum.ORDER_RETURN_PENDING != OrderReturnStatusEnum.getByCode(orderReturnDO.getReturnStatus())) {
            throw new BusinessException(OrderErrorCode.ORDER_RETURN_STATUS_ERROR);
        }

        OrderReturnTypeEnum orderReturnTypeEnum = OrderReturnTypeEnum.getByCode(orderReturnDO.getReturnType());
        if (null == orderReturnTypeEnum) {
            throw new BusinessException(OrderErrorCode.ORDER_RETURN_TYPE_ERROR);
        }
    }

    /**
     * 审核通过时请求数据的校验
     *
     * @param request 退货单审核请求参数
     * @param orderDTO 订单信息
     * @param orderDeliveryDOMap 订单发货信息的map
     */
    private void checkVerifyApprovedRequest(OrderReturnVerifyRequest request, OrderDTO orderDTO, Map<String, OrderDeliveryDO> orderDeliveryDOMap) {
        // 审核的时候必须已经开发票(以岭供应商)
        boolean isExistsInvoice = 1 == request.getIsYiLingInvoice() && (!OrderInvoiceApplyStatusEnum.INVOICED.getCode().equals(orderDTO.getInvoiceStatus()) && !OrderInvoiceApplyStatusEnum.NOT_NEED.getCode().equals(orderDTO.getInvoiceStatus()));
        if (isExistsInvoice) {
            throw new BusinessException(OrderErrorCode.ORDER_RETURN_INVOICE_STATUS_ERROR);
        }

        // 批次退货数量不能大于批次发货数量
        checkOrderReturnDetailBatch(request, orderDeliveryDOMap);
    }

    /**
     * 退货单驳回操作
     *
     * @param request 退货单审核不通过请求参数
     * @param orderReturnDO 退货单信息
     */
    private void rejectOrderReturn(OrderReturnVerifyRequest request, OrderReturnDO orderReturnDO) {
        log.info("rejectReturnOrder start request:[{}]", JSONUtil.toJsonStr(request));
        // 修改退货单状态
        OrderReturnDO returnDO = PojoUtils.map(request, OrderReturnDO.class);
        returnDO.setId(request.getReturnId());
        returnDO.setFailReason(request.getFailReason());
        returnDO.setReturnStatus(OrderReturnStatusEnum.ORDER_RETURN_REJECT.getCode());
        returnDO.setReturnAuditUser(request.getOpUserId());
        returnDO.setReturnAuditTime(request.getOpTime());
        returnDO.setOpUserId(request.getOpUserId());
        returnDO.setOpTime(request.getOpTime());
        log.info("rejectReturnOrder updateById OrderReturnDO:[{}]", JSONUtil.toJsonStr(returnDO));
        orderReturnService.updateById(returnDO);

        // 需要将用户收货数据修复
        returnReceiveQuality(orderReturnDO, request.getOpUserId(), request.getOpTime());

        orderLogService.add(orderReturnDO.getId(), 2, "供应商驳回退货单");
    }

    /**
     * 退货单审核通过
     *
     * @param request 请求参数
     * @param orderReturnDO 退货单信息
     */
    private void approvedOrderReturn(OrderReturnVerifyRequest request, OrderReturnDO orderReturnDO) {
        log.info("approvedOrderReturn start request:[{}]", JSONUtil.toJsonStr(request));
        // 退货单审核:1.采购商退货，2.破损退货
        // 注意:退货数量是可以修改的(采购商退货-审核时可改，破损退货-反审核可改)

        OrderDTO orderDTO = orderService.getOrderInfo(orderReturnDO.getOrderId());
        // 退货类型
        OrderReturnTypeEnum orderReturnTypeEnum = OrderReturnTypeEnum.getByCode(orderReturnDO.getReturnType());

        // 订单发货信息
        List<OrderDeliveryDO> orderDeliveryDOList = orderDeliveryService.getOrderDeliveryList(request.getOrderId());
        Map<String, OrderDeliveryDO> orderDeliveryDOMap = orderDeliveryDOList.stream().collect(Collectors.toMap(k -> k.getDetailId() + "-" + k.getBatchNo(), o -> o, (k1, k2) -> k1));

        log.info("checkOrderReturn request :[{}], orderReturnDO :[{}], orderDTO :[{}], orderReturnTypeEnum :[{}]", request, orderReturnDO, orderDTO, orderReturnTypeEnum);

        // 审核通过时请求数据的校验
        checkVerifyApprovedRequest(request, orderDTO, orderDeliveryDOMap);

        List<OrderDetailRequest> orderDetailList = request.getOrderDetailList();
        BigDecimal totalGoodsTicketDiscountPrice = BigDecimal.ZERO;
        BigDecimal totalGoodsCashDiscountPrice = BigDecimal.ZERO;
        BigDecimal totalGoodsReturnAmount = BigDecimal.ZERO;

        // 遍历每种退货的商品，对每种退货商品使用的票折退回
        for (OrderDetailRequest orderDetailRequest : orderDetailList) {
            //数量改变的时候需要重新计算票折金额和退货单总金额
            List<OrderDeliveryRequest> orderDeliveryList = orderDetailRequest.getOrderDeliveryList();
            int returnQuantity = 0;

            for (OrderDeliveryRequest orderDeliveryRequest : orderDeliveryList) {

                OrderReturnDetailBatchDO orderReturnDetailBatchDO = orderReturnDetailBatchService.queryByReturnIdAndDetailIdAndBathNo(orderReturnDO.getId(), orderDetailRequest.getDetailId(), orderDeliveryRequest.getBatchNo());
                if (null != orderReturnDetailBatchDO && orderDeliveryRequest.getReturnQuantity().compareTo(orderReturnDetailBatchDO.getReturnQuantity()) != 0) {
                    // 校验退货数量是否符合要求
                    //                    checkOrderReturnDetail(orderReturnDO.getId(), orderReturnTypeEnum, orderDetailRequest, orderDeliveryRequest, orderDTO.getId());

                    // 如果退货数量有改动
                    log.info("checkOrderReturn, orderReturnDetailBatchDO :[{}]", orderReturnDetailBatchDO);

                    orderReturnDetailBathCheckReturn(orderReturnDetailBatchDO.getId(), orderDeliveryRequest.getReturnQuantity(), request.getOpUserId(), request.getOpTime());
                }
                // 当请求的退货商品批次明细不存在的时候，需要新增退货单明细批次信息
                if (null == orderReturnDetailBatchDO && orderDeliveryRequest.getReturnQuantity() > 0) {
                    OrderDeliveryDO orderDeliveryDO = orderDeliveryDOMap.get(orderDetailRequest.getDetailId() + "-" + orderDeliveryRequest.getBatchNo());
                    orderReturnDetailBatchDO = saveOrderReturnDetailBatchDO(orderReturnTypeEnum, orderDeliveryRequest, orderDetailRequest.getDetailId(), orderDeliveryDO);
                    orderReturnDetailBatchDO.setReturnId(orderReturnDO.getId());
                    orderReturnDetailBatchDO.setOpUserId(request.getOpUserId());
                    orderReturnDetailBatchDO.setOpTime(request.getOpTime());
                    log.info("checkOrderReturn, addBatch orderReturnDetailBatchDO :[{}]", JSONUtil.toJsonStr(orderReturnDetailBatchDO));

                    orderReturnDetailBatchService.save(orderReturnDetailBatchDO);
                }

                // 新增退货出库单erp信息数据
                saveOrderDeliveryErp(orderReturnDetailBatchDO, request.getOpUserId(), request.getOpTime());

                returnQuantity += orderDeliveryRequest.getReturnQuantity();

                // 发货批次数量修改
                orderDeliveryCheckReturn(orderReturnDO.getOrderId(), orderDetailRequest.getDetailId(), orderDeliveryRequest.getBatchNo(), orderDeliveryRequest.getReturnQuantity(), request.getOpUserId(), request.getOpTime());
            }
            // 首先先将这个退货单的退货数量写入订单明细修改表中
            OrderReturnDetailDO orderReturnDetailDO = orderReturnDetailCheckReturn(orderDetailRequest.getDetailId(), orderReturnDO.getId(), returnQuantity, request.getOpUserId(), request.getOpTime());

            // 修改收货的 数量和金额
            orderDetailChangeCheckReturn(orderDetailRequest.getDetailId());

            totalGoodsReturnAmount = totalGoodsReturnAmount.add(orderReturnDetailDO.getReturnAmount());
            totalGoodsCashDiscountPrice = totalGoodsCashDiscountPrice.add(orderReturnDetailDO.getReturnCashDiscountAmount());
            totalGoodsTicketDiscountPrice = totalGoodsTicketDiscountPrice.add(orderReturnDetailDO.getReturnTicketDiscountAmount());

            BigDecimal totalTicketDiscountPrice = orderReturnDetailDO.getReturnTicketDiscountAmount();
            if (totalTicketDiscountPrice.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }

            // 票折回滚
            returnTicketDiscountRecord(totalTicketDiscountPrice, orderReturnDO.getOrderId(), orderDetailRequest.getDetailId());
        }

        // 审核通过的时候修复一下退货单明细表里面的相关的退货金额(票折，现折)(由于退货商品审核时可以修改，所以才需要)
        updateOrderReturn(orderReturnDO, totalGoodsReturnAmount, totalGoodsTicketDiscountPrice, totalGoodsCashDiscountPrice, request.getOpUserId(), request.getOpTime());

        // 订单写入总退款金额
        operateOrderByReturn(orderDTO.getId());

        // 日志
        orderLogService.add(orderReturnDO.getId(), 2, "供应商审核通过退货单");
    }


    /**
     * 退货单审核通过时，修改退货单信息(退货审核时，退货数量可以修改)
     *
     * @param orderReturnDO 退货单信息
     * @param totalGoodsReturnAmount 退货总金额
     * @param totalGoodsTicketDiscountPrice 退货总票折
     * @param totalGoodsCashDiscountPrice 退货总现折
     * @param opUserId 操作人
     * @param opTime 操作世界
     */
    private void updateOrderReturn(OrderReturnDO orderReturnDO, BigDecimal totalGoodsReturnAmount, BigDecimal totalGoodsTicketDiscountPrice, BigDecimal totalGoodsCashDiscountPrice, Long opUserId, Date opTime) {
        orderReturnDO.setReturnStatus(OrderReturnStatusEnum.ORDER_RETURN_PASS.getCode());
        orderReturnDO.setReturnAuditTime(opTime);
        orderReturnDO.setReturnAuditUser(opUserId);
        // 如果退货数量有改动，则需要修改退货单总额已经总票折和现折
        orderReturnDO.setReturnAmount(totalGoodsReturnAmount);
        orderReturnDO.setTicketDiscountAmount(totalGoodsTicketDiscountPrice);
        orderReturnDO.setCashDiscountAmount(totalGoodsCashDiscountPrice);
        orderReturnDO.setOpUserId(opUserId);
        orderReturnDO.setOpTime(opTime);
        log.info("checkOrderReturn updateOrderReturn orderReturnDO :[{}]", JSONUtil.toJsonStr(orderReturnDO));
        // 退货单修改审核时间(数量有修改时需要修改金额)
        orderReturnService.updateById(orderReturnDO);
    }

    /**
     * 退货单审核通过时，退货明细变更信息数据修正
     *
     * @param detailId 订单明细id
     * @param returnId 退货单id
     * @param returnQuantity 退货数量
     * @param opUserId 操作人
     * @param opTime 操作时间
     * @return 退货单明细(退货数量, 退货总额, 退货现折总额, 退货票折总额赋值)
     */
    private OrderReturnDetailDO orderReturnDetailCheckReturn(Long detailId, Long returnId, Integer returnQuantity, Long opUserId, Date opTime) {
        OrderDetailChangeBO orderDetailChangeBO = orderDetailChangeService.updateReturnData(detailId, returnQuantity, true);
        OrderReturnDetailDO orderReturnDetailDO = orderReturnDetailService.queryByReturnIdAndDetailId(returnId, detailId);
        log.info("checkOrderReturn orderReturnDetailCheckReturn, orderDetailChangeBO :[{}], orderReturnDetailDO :[{}]", orderDetailChangeBO, orderReturnDetailDO);

        OrderReturnDetailDO returnDetailDO = new OrderReturnDetailDO();
        returnDetailDO.setId(orderReturnDetailDO.getId());
        returnDetailDO.setReturnQuantity(returnQuantity);
        returnDetailDO.setReturnAmount(orderDetailChangeBO.getAmount());
        returnDetailDO.setReturnCashDiscountAmount(orderDetailChangeBO.getCashDiscountAmount());
        returnDetailDO.setReturnTicketDiscountAmount(orderDetailChangeBO.getTicketDiscountAmount());
        returnDetailDO.setOpUserId(opUserId);
        returnDetailDO.setOpTime(opTime);
        log.info("checkOrderReturn orderReturnDetailCheckReturn, orderReturnDetailDONew :[{}]", returnDetailDO);
        orderReturnDetailService.updateById(returnDetailDO);

        orderReturnDetailDO.setReturnQuantity(returnQuantity);
        orderReturnDetailDO.setReturnAmount(orderDetailChangeBO.getAmount());
        orderReturnDetailDO.setReturnCashDiscountAmount(orderDetailChangeBO.getCashDiscountAmount());
        orderReturnDetailDO.setReturnTicketDiscountAmount(orderDetailChangeBO.getTicketDiscountAmount());
        return orderReturnDetailDO;
    }

    /**
     * 退货单审核驳回时-将收货数量退回
     *
     * @param orderReturnDO 退货单信息
     * @param opUserId 操作人id
     * @param opTime 操作时间
     */
    private void returnReceiveQuality(OrderReturnDO orderReturnDO, Long opUserId, Date opTime) {
        List<OrderReturnDetailDO> orderReturnDetailDOList = orderReturnDetailService.getOrderReturnDetailByReturnId(orderReturnDO.getId());
        for (OrderReturnDetailDO orderReturnDetailDO : orderReturnDetailDOList) {
            orderDetailChangeCheckReturn(orderReturnDetailDO.getDetailId());
        }
        List<OrderReturnDetailBatchDO> orderReturnDetailBatchDOList = orderReturnDetailBatchService.getOrderReturnDetailBatch(orderReturnDO.getId(), null, null);
        for (OrderReturnDetailBatchDO orderReturnDetailBatchDO : orderReturnDetailBatchDOList) {
            orderDeliveryCheckReturn(orderReturnDO.getOrderId(), orderReturnDetailBatchDO.getDetailId(), orderReturnDetailBatchDO.getBatchNo(), 0, opUserId, opTime);
        }
    }

    /**
     * 退货单审核驳回时-将发货表里面的收货数量退回
     *
     * @param orderId 订单id
     * @param detailId 订单明细id
     * @param bathNo 发货批次号
     * @param returnQuantity 退回数量
     * @param opUserId 操作人id
     * @param opTime 操作时间
     */
    private void orderDeliveryCheckReturn(Long orderId, Long detailId, String bathNo, Integer returnQuantity, Long opUserId, Date opTime) {
        List<OrderReturnDetailBatchDO> orderReturnDetailBatchDOList = orderReturnDetailBatchService.queryByDetailIdAndBatchNoAndType(detailId, bathNo, null);
        Integer returnBatchQuality = orderReturnDetailBatchDOList.stream().mapToInt(OrderReturnDetailBatchDO::getReturnQuantity).sum();

        OrderDeliveryDO orderDeliveryDO = orderDeliveryService.queryByOrderIdAndDetailIdAndBatchNo(orderId, detailId, bathNo);
        Integer deliveryQuantity = orderDeliveryDO.getDeliveryQuantity();

        // 修正订单发货表里面的收货数量
        OrderDeliveryDO orderDeliveryDONew = new OrderDeliveryDO();
        orderDeliveryDONew.setId(orderDeliveryDO.getId());
        orderDeliveryDONew.setReceiveQuantity(deliveryQuantity - returnBatchQuality - returnQuantity);
        orderDeliveryDONew.setOpUserId(opUserId);
        orderDeliveryDONew.setOpTime(opTime);
        orderDeliveryService.updateById(orderDeliveryDONew);
    }

    /**
     * 更新订单明细修改表里面的收货数量
     *
     * @param detailId 订单明细id
     */
    private void orderDetailChangeCheckReturn(Long detailId) {
        OrderDetailChangeDO orderDetailChangeDO = orderDetailChangeService.getByDetailId(detailId);
        orderDetailChangeService.updateReceiveData(detailId, orderDetailChangeDO.getDeliveryQuantity() - orderDetailChangeDO.getReturnQuantity());
    }

    /**
     * 根据退货单明细批次信息修改erp出库单信息
     *
     * @param orderReturnDetailBatchDO 退货单批次信息
     */
    private void saveOrderDeliveryErp(OrderReturnDetailBatchDO orderReturnDetailBatchDO, Long opUserId, Date opTime) {
        if (null == orderReturnDetailBatchDO) {
            return;
        }
        List<OrderDeliveryErpDO> orderDeliveryErpDOList = orderDeliveryErpService.listByDetailIdAndBatchNo(null, orderReturnDetailBatchDO.getDetailId(), orderReturnDetailBatchDO.getBatchNo());
        if (orderDeliveryErpDOList.size() < 1) {
            return;
        }
        Integer returnQuantity = orderReturnDetailBatchDO.getReturnQuantity();
        List<OrderReturnDetailErpDO> entityList = new ArrayList<>();
        log.info("checkOrderReturn saveOrderDeliveryErp, returnQuantity to batch :[{}], orderDeliveryErpDOList :[{}]", returnQuantity, orderDeliveryErpDOList);

        // 批次退货数量分摊到各个出库单号上面
        for (OrderDeliveryErpDO orderDeliveryErpDO : orderDeliveryErpDOList) {
            Integer deliveryQuantity = orderDeliveryErpDO.getDeliveryQuantity();
            // 此出库单号已经退货数量
            Integer allReturnQuality = orderReturnDetailErpService.sumReturnQualityByErpDeliveryNo(orderDeliveryErpDO.getDetailId(), orderDeliveryErpDO.getBatchNo(), orderDeliveryErpDO.getErpDeliveryNo(), StringUtils.isNotBlank(orderDeliveryErpDO.getErpSendOrderId()) ? orderDeliveryErpDO.getErpSendOrderId() : null);
            // 剩余的可退数量
            int lastReturnQuality = deliveryQuantity - allReturnQuality;
            if (returnQuantity > 0 && lastReturnQuality > 0) {
                OrderReturnDetailErpDO orderReturnDetailErpDO = new OrderReturnDetailErpDO()
                        .setReturnId(orderReturnDetailBatchDO.getReturnId())
                        .setDetailId(orderDeliveryErpDO.getDetailId())
                        .setBatchNo(orderDeliveryErpDO.getBatchNo())
                        .setExpiryDate(orderReturnDetailBatchDO.getExpiryDate())
                        .setErpDeliveryNo(orderDeliveryErpDO.getErpDeliveryNo());
                orderReturnDetailErpDO.setErpSendOrderId(orderDeliveryErpDO.getErpSendOrderId());
                orderReturnDetailErpDO.setOpUserId(opUserId);
                orderReturnDetailErpDO.setOpTime(opTime);
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
        if (entityList.size() > 0) {
            orderReturnDetailErpService.saveBatch(entityList);
        }
    }

    /**
     * 退货单批次根据id修改数量
     *
     * @param id 退货单明细批次表id
     * @param returnQuality 此批次退货数量
     */
    private void orderReturnDetailBathCheckReturn(Long id, Integer returnQuality, Long opUserId, Date opTime) {
        OrderReturnDetailBatchDO orderReturnDetailBatchDO = new OrderReturnDetailBatchDO();
        orderReturnDetailBatchDO.setId(id);
        orderReturnDetailBatchDO.setReturnQuantity(returnQuality);
        orderReturnDetailBatchDO.setOpUserId(opUserId);
        orderReturnDetailBatchDO.setOpTime(opTime);
        orderReturnDetailBatchService.updateById(orderReturnDetailBatchDO);
    }

    /**
     * 采购商退货的时候将收货数量减少
     *
     * @param orderId 订单id
     * @param detailId 订单明细id
     * @param bathNo 批次号
     * @param returnQuality 此订单明细批次的退货数量
     */
    private void saveOrderDelivery(Long orderId, Long detailId, String bathNo, Integer returnQuality) {
        OrderDeliveryDO orderDeliveryDO = orderDeliveryService.queryByOrderIdAndDetailIdAndBatchNo(orderId, detailId, bathNo);
        OrderDeliveryDO orderDeliveryDONew = new OrderDeliveryDO();
        orderDeliveryDONew.setId(orderDeliveryDO.getId());
        Integer receiveQuantity = orderDeliveryDO.getReceiveQuantity();
        if (receiveQuantity < returnQuality) {
            throw new BusinessException(OrderErrorCode.ORDER_RETURN_QUANTITY_OVERSTEP);
        }
        orderDeliveryDONew.setReceiveQuantity(receiveQuantity - returnQuality);
        orderDeliveryService.updateById(orderDeliveryDONew);
    }

    /**
     * 采购商退货的时候将订单明细变动表里面的收货数量减少
     *
     * @param detailId 订单明细id
     * @param returnQuality 此订单明细的退货数量
     */
    private void saveOrderDetailChange(Long detailId, Integer returnQuality) {
        OrderDetailChangeDO orderDetailChangeDO = orderDetailChangeService.getByDetailId(detailId);
        orderDetailChangeService.updateReceiveData(detailId, orderDetailChangeDO.getReceiveQuantity() - returnQuality);
    }

    /**
     * 退货单申请时的校验
     *
     * @param request 退货单申请数据
     * @param orderDTO 订单信息
     * @param orderReturnTypeEnum 退货单类型
     */
    private void checkOrderReturn(OrderReturnApplyRequest request, OrderDTO orderDTO, OrderReturnTypeEnum orderReturnTypeEnum, Map<String, OrderDeliveryDO> orderDeliveryDOMap) {
        // 退货单明细不能为空
        checkOrderReturnDetailNotEmpty(request);

        // 上面的是公用的校验；下面是供应商退货单之外的校验
        if (OrderReturnTypeEnum.SELLER_RETURN_ORDER == orderReturnTypeEnum) {
            return;
        }

        // 批次退货数量不能大于批次发货数量
        checkOrderReturnDetailBatch(request, orderDeliveryDOMap);

        // 跨年订单不允许申请退货
        checkReturnOrderDate(orderDTO);

        //判断订单是否已经收货
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.RECEIVED.getCode())) {
            throw new BusinessException(OrderErrorCode.ORDER_RETURN_ORDER_STATUS_ERROR);
        }
        int count = orderReturnService.countByOrderIdAndStatus(orderDTO.getId(), OrderReturnStatusEnum.ORDER_RETURN_PENDING.getCode());
        if (count > 0) {
            throw new BusinessException(OrderErrorCode.ORDER_RETURN_STATUS_IS_AUDIT_EXIST);
        }
    }

    /**
     * 批次退货数量不能大于批次发货数量
     *
     * @param request 退货单申请参数
     */
    private void checkOrderReturnDetailBatch(OrderReturnApplyRequest request, Map<String, OrderDeliveryDO> orderDeliveryDOMap) {
        List<OrderDetailRequest> orderDetailList = request.getOrderDetailList();
        for (OrderDetailRequest detailRequest : orderDetailList) {
            List<OrderDeliveryRequest> deliveryList = detailRequest.getOrderDeliveryList();
            for (OrderDeliveryRequest deliveryRequest : deliveryList) {
                OrderDeliveryDO orderDeliveryDO = orderDeliveryDOMap.get(detailRequest.getDetailId() + "-" + deliveryRequest.getBatchNo());
                List<OrderReturnDetailBatchDO> returnBatchList = orderReturnDetailBatchService.queryByDetailIdAndBatchNoAndType(detailRequest.getDetailId(), deliveryRequest.getBatchNo(), null);
                int returnQuality = deliveryRequest.getReturnQuantity();
                int haveReturnQuality = returnBatchList.stream().mapToInt(OrderReturnDetailBatchDO::getReturnQuantity).sum();
                if (returnQuality + haveReturnQuality > orderDeliveryDO.getDeliveryQuantity()) {
                    throw new BusinessException(OrderErrorCode.ORDER_RETURN_QUANTITY_OVERSTEP);
                }
            }
        }
    }

    /**
     * 退货单明细不能为空
     *
     * @param request 退货单申请数据
     */
    private void checkOrderReturnDetailNotEmpty(OrderReturnApplyRequest request) {
        List<OrderDetailRequest> orderDetailList = request.getOrderDetailList();
        if (CollectionUtil.isEmpty(orderDetailList)) {
            throw new BusinessException(OrderErrorCode.ORDER_RETURN_FORM_ERROR);
        }
    }

    /**
     * 跨年订单不允许申请退货
     *
     * @param orderDTO 订单表
     */
    private void checkReturnOrderDate(OrderDTO orderDTO) {
        Date createTime = orderDTO.getCreateTime();
        int createYear = DateUtil.year(createTime);
        int nowYear = DateUtil.year(new Date());
        if (createYear != nowYear) {
            throw new BusinessException(OrderErrorCode.ORDER_RETURN_OTHER_YEAR);
        }
    }

    /**
     * 退货单相关信息的创建
     *
     * @param request 申请退货单请求参数
     * @param orderDTO 退货对应的订单表信息
     * @param orderReturnTypeEnum 申请的退货单类型
     * @return 退货单
     */
    private OrderReturnDO saveApplyReturnOrder(OrderReturnApplyRequest request, OrderDTO orderDTO, OrderReturnTypeEnum orderReturnTypeEnum) {
        log.info("createOrderReturn saveApplyReturnOrder, request :[{}], orderDTO :[{}], orderReturnTypeEnum :[{}]", request, orderDTO, orderReturnTypeEnum.getName());

        // 订单发货信息
        List<OrderDeliveryDO> orderDeliveryDOList = orderDeliveryService.getOrderDeliveryList(request.getOrderId());
        Map<String, OrderDeliveryDO> orderDeliveryDOMap = orderDeliveryDOList.stream().collect(Collectors.toMap(k -> k.getDetailId() + "-" + k.getBatchNo(), o -> o, (k1, k2) -> k1));

        // 退货申请信息校验
        checkOrderReturn(request, orderDTO, orderReturnTypeEnum, orderDeliveryDOMap);

        //如果此时有其他此订单的退货单在审核状态，则不允许退货
        //查询商品单价
        List<OrderDetailRequest> orderDetailList = request.getOrderDetailList();
        BigDecimal totalReturnAmount = BigDecimal.ZERO;
        BigDecimal totalCashDiscountPrice = BigDecimal.ZERO;
        BigDecimal totalTicketDiscountPrice = BigDecimal.ZERO;
        List<OrderReturnDetailDO> orderReturnDetailDOList = new ArrayList<>();
        List<OrderReturnDetailBatchDO> orderReturnDetailBatchDOAddList = new ArrayList<>();
        // 订单明细信息
        List<OrderDetailDTO> orderDetailDTOList = orderDetailService.getOrderDetailInfo(request.getOrderId());
        Map<Long, OrderDetailDTO> detailDTOMap = orderDetailDTOList.stream().collect(Collectors.toMap(OrderDetailDTO::getId, o -> o, (k1, k2) -> k1));

        // 退货单里面的每样退货商品遍历
        for (OrderDetailRequest orderDetailRequest : orderDetailList) {
            OrderDetailDTO orderDetail = detailDTOMap.get(orderDetailRequest.getDetailId());
            log.info("createOrderReturn saveApplyReturnOrder, orderDetailDTO :[{}]", orderDetail);

            List<OrderDeliveryRequest> orderDeliveryList = orderDetailRequest.getOrderDeliveryList();
            int returnQuantity = 0;
            // 注意：供应商退货没有批次概念
            // 退货单里面此件商品的对应发货单遍历，因为退货的商品是与发货单里面的批次对应的
            for (OrderDeliveryRequest orderDeliveryRequest : orderDeliveryList) {
                OrderDeliveryDO orderDeliveryDO = orderDeliveryDOMap.get(orderDetailRequest.getDetailId() + "-" + orderDeliveryRequest.getBatchNo());
                //                checkOrderReturnDetail(null, orderReturnTypeEnum, orderDetailRequest, orderDeliveryRequest, orderDTO.getId());
                // 添加退货单明细批次表数据，传入的发货信息数据可能为空(供应商退货)
                if (0 != orderDeliveryRequest.getReturnQuantity()) {
                    OrderReturnDetailBatchDO orderReturnDetailBatchDO = saveOrderReturnDetailBatchDO(orderReturnTypeEnum, orderDeliveryRequest, orderDetailRequest.getDetailId(), orderDeliveryDO);
                    orderReturnDetailBatchDOAddList.add(orderReturnDetailBatchDO);
                    returnQuantity = returnQuantity + orderDeliveryRequest.getReturnQuantity();
                }
            }

            if (0 != returnQuantity) {
                OrderReturnDetailDO orderReturnDetailDO = saveOrderReturnDetail(orderReturnTypeEnum, orderDetail, returnQuantity);
                orderReturnDetailDOList.add(orderReturnDetailDO);

                totalReturnAmount = totalReturnAmount.add(orderReturnDetailDO.getReturnAmount());
                totalCashDiscountPrice = totalCashDiscountPrice.add(orderReturnDetailDO.getReturnCashDiscountAmount());
                totalTicketDiscountPrice = totalTicketDiscountPrice.add(orderReturnDetailDO.getReturnTicketDiscountAmount());
            }
            // 订单明细表里面
            //供应商退货的时候，申请1.需要将退货数量回写到订单明细表2.需要回滚票折
            if (orderReturnTypeEnum.getCode().equals(OrderReturnTypeEnum.SELLER_RETURN_ORDER.getCode())) {
                if (totalTicketDiscountPrice.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }
                returnTicketDiscountRecord(totalTicketDiscountPrice, orderDetail.getOrderId(), orderDetail.getId());
            }
        }

        // 退货单表信息存储
        OrderReturnDO orderReturnDO = saveOrderReturn(orderReturnTypeEnum, orderDTO, request);
        orderReturnDO.setReturnAmount(totalReturnAmount);
        orderReturnDO.setCashDiscountAmount(totalCashDiscountPrice);
        orderReturnDO.setTicketDiscountAmount(totalTicketDiscountPrice);
        log.info("createOrderReturn saveApplyReturnOrder, orderReturnDO :[{}]", orderReturnDO);
        orderReturnService.save(orderReturnDO);

        // 退货单明细补充退货单id
        orderReturnDetailDOList.forEach(e -> {
            e.setReturnId(orderReturnDO.getId());
            e.setOpUserId(request.getOpUserId());
            e.setOpTime(request.getOpTime());
        });

        // 退货单明细批次补充退货单id字段
        orderReturnDetailBatchDOAddList.forEach(e -> {
            e.setReturnId(orderReturnDO.getId());
            e.setOpUserId(request.getOpUserId());
            e.setOpTime(request.getOpTime());
        });

        if (orderReturnTypeEnum.getCode().equals(OrderReturnTypeEnum.SELLER_RETURN_ORDER.getCode())) {
            operateOrderByReturn(orderDTO.getId());
        }

        // 退货单明细信息
        log.info("createOrderReturn saveApplyReturnOrder, orderReturnDetailDOList:[{}]", orderReturnDetailDOList);
        orderReturnDetailService.saveBatch(orderReturnDetailDOList);

        // 退货单明细批次信息
        log.info("createOrderReturn saveApplyReturnOrder, orderReturnDetailBatchDOList:[{}]", orderReturnDetailBatchDOAddList);
        orderReturnDetailBatchService.saveBatch(orderReturnDetailBatchDOAddList);
        return orderReturnDO;
    }

    /**
     * 退货单明细批次信息
     *
     * @param orderReturnTypeEnum 退货单类型
     * @param orderDeliveryRequest 退货单申请的发货信息
     * @param detailId 订单明细
     * @param orderDeliveryDO 订单发货信息
     * @return 退货单明细批次信息
     */
    private OrderReturnDetailBatchDO saveOrderReturnDetailBatchDO(OrderReturnTypeEnum orderReturnTypeEnum, OrderDeliveryRequest orderDeliveryRequest, Long detailId, OrderDeliveryDO orderDeliveryDO) {
        OrderReturnDetailBatchDO orderReturnDetailBatchDO = new OrderReturnDetailBatchDO();
        orderReturnDetailBatchDO.setDetailId(detailId);
        orderReturnDetailBatchDO.setBatchNo(orderDeliveryRequest.getBatchNo());
        if (orderReturnTypeEnum != OrderReturnTypeEnum.SELLER_RETURN_ORDER) {
            // 供应商退货单没有发货信息，所以有效期不存在
            orderReturnDetailBatchDO.setExpiryDate(orderDeliveryDO.getExpiryDate());
        }
        orderReturnDetailBatchDO.setReturnQuantity(orderDeliveryRequest.getReturnQuantity());
        orderReturnDetailBatchDO.setRemark("");
        return orderReturnDetailBatchDO;
    }

    /**
     * 退货单明细表数据组合
     *
     * @param orderReturnTypeEnum 退货单类型
     * @param orderDetailDO 订单明细表信息
     * @param returnQuantity 退货数量
     * @return 退货单明细信息
     */
    private OrderReturnDetailDO saveOrderReturnDetail(OrderReturnTypeEnum orderReturnTypeEnum, OrderDetailDTO orderDetailDO, int returnQuantity) {
        OrderReturnDetailDO orderReturnDetailDO = new OrderReturnDetailDO();
        orderReturnDetailDO.setDetailId(orderDetailDO.getId());
        orderReturnDetailDO.setStandardId(orderDetailDO.getStandardId());
        orderReturnDetailDO.setGoodsId(orderDetailDO.getGoodsId());
        orderReturnDetailDO.setGoodsSkuId(orderDetailDO.getGoodsSkuId());
        orderReturnDetailDO.setGoodsErpCode(orderDetailDO.getGoodsErpCode());
        orderReturnDetailDO.setReturnQuantity(returnQuantity);
        if (orderReturnTypeEnum.getCode().equals(OrderReturnTypeEnum.BUYER_RETURN_ORDER.getCode())) {
            // 采购商退货单   审核的时候才能将退货数据添加到订单明细变更信息表
            OrderDetailChangeBO orderDetailChangeBO = orderDetailChangeService.updateReturnData(orderDetailDO.getId(), returnQuantity, false);
            orderReturnDetailDO.setReturnAmount(orderDetailChangeBO.getAmount());
            orderReturnDetailDO.setReturnCashDiscountAmount(orderDetailChangeBO.getCashDiscountAmount());
            orderReturnDetailDO.setReturnTicketDiscountAmount(orderDetailChangeBO.getTicketDiscountAmount());
        } else if (orderReturnTypeEnum.getCode().equals(OrderReturnTypeEnum.SELLER_RETURN_ORDER.getCode())) {
            // 供应商退货    发货的时候已经将退货数据添加到订单明细变更信息表了
            OrderDetailChangeDO orderDetailChangeDO = orderDetailChangeService.getByDetailId(orderDetailDO.getId());
            orderReturnDetailDO.setReturnAmount(orderDetailChangeDO.getSellerReturnAmount());
            orderReturnDetailDO.setReturnCashDiscountAmount(orderDetailChangeDO.getSellerReturnCashDiscountAmount());
            orderReturnDetailDO.setReturnTicketDiscountAmount(orderDetailChangeDO.getSellerReturnTicketDiscountAmount());
        } else if (orderReturnTypeEnum.getCode().equals(OrderReturnTypeEnum.DAMAGE_RETURN_ORDER.getCode())) {
            // 破损退货   审核的时候才能将退货数据添加到订单明细变更信息表
            OrderDetailChangeBO orderDetailChangeBO = orderDetailChangeService.updateReturnData(orderDetailDO.getId(), returnQuantity, false);
            orderReturnDetailDO.setReturnAmount(orderDetailChangeBO.getAmount());
            orderReturnDetailDO.setReturnCashDiscountAmount(orderDetailChangeBO.getCashDiscountAmount());
            orderReturnDetailDO.setReturnTicketDiscountAmount(orderDetailChangeBO.getTicketDiscountAmount());
        }
        return orderReturnDetailDO;
    }

    /**
     * 票折回滚--根据订单明细id来退票折
     * 先从订单明细票折信息表查询出使用科哪些票折
     * 根据查询出来的票折单号，以创建时间升序来查询票折信息
     * 再根据票折信息表一个个分配退还的票折
     *
     * @param totalTicketDiscountPrice 总退还票折金额
     * @param orderId 订单id
     * @param detailId 订单明细id
     */
    private void returnTicketDiscountRecord(BigDecimal totalTicketDiscountPrice, Long orderId, Long detailId) {
        log.info("returnTicketDiscountRecord, totalTicketDiscountPrice :[{}], orderId :[{}], detailId :[{}]", totalTicketDiscountPrice, orderId, detailId);
        // 1.票折回滚(一个订单明细可能使用了多张票折)
        List<OrderDetailTicketDiscountDO> orderDetailTicketDiscountDOList = orderDetailTicketDiscountService.getByOrderIdAndDetailId(orderId, detailId);
        log.info("returnTicketDiscountRecord, orderDetailTicketDiscountDOList :[{}]", orderDetailTicketDiscountDOList);

        Map<String, List<OrderDetailTicketDiscountDO>> listMap = orderDetailTicketDiscountDOList.stream().collect(Collectors.groupingBy(OrderDetailTicketDiscountDO::getTicketDiscountNo));

        List<String> ticketDiscountNoList = orderDetailTicketDiscountDOList.stream().map(OrderDetailTicketDiscountDO::getTicketDiscountNo).collect(Collectors.toList());

        QueryWrapper<TicketDiscountRecordDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(TicketDiscountRecordDO::getTicketDiscountNo, ticketDiscountNoList).orderByAsc(TicketDiscountRecordDO::getCreateTime);
        List<TicketDiscountRecordDO> ticketDiscountRecordDOList = ticketDiscountRecordService.list(wrapper);
        log.info("returnTicketDiscountRecord, ticketDiscountRecordDOList :[{}]", ticketDiscountRecordDOList);

        List<OrderTicketDiscountDO> orderTicketDiscountDOList = orderTicketDiscountService.listByOrderId(orderId);
        Map<String, OrderTicketDiscountDO> orderTicketDiscountDOMap = orderTicketDiscountDOList.stream().collect(Collectors.toMap(k -> k.getTicketDiscountNo() + k.getOrderId() + k.getApplyId(), e -> e));

        for (TicketDiscountRecordDO ticketDiscountRecordDO : ticketDiscountRecordDOList) {
            List<OrderDetailTicketDiscountDO> orderDetailTicketDiscountDOS = listMap.get(ticketDiscountRecordDO.getTicketDiscountNo());
            for (OrderDetailTicketDiscountDO orderDetailTicketDiscountDO : orderDetailTicketDiscountDOS) {
                log.info("returnTicketDiscountRecord, orderDetailTicketDiscountDO :[{}]", orderDetailTicketDiscountDO);

                // 如果回滚金额小于或等于此票折总金额，则
                BigDecimal allowReturnAmount = orderDetailTicketDiscountDO.getUseAmount().subtract(orderDetailTicketDiscountDO.getReturnAmount());
                if (allowReturnAmount.compareTo(BigDecimal.ZERO) < 0) {
                    continue;
                }
                // 订单票折记录 退还票折
                OrderTicketDiscountDO orderTicketDiscountDO = orderTicketDiscountDOMap.get(orderDetailTicketDiscountDO.getTicketDiscountNo() + orderDetailTicketDiscountDO.getOrderId() + orderDetailTicketDiscountDO.getApplyId());
                if (totalTicketDiscountPrice.compareTo(allowReturnAmount) < 1) {
                    ticketDiscountRecordService.reduceUsedAmount(ticketDiscountRecordDO.getTicketDiscountNo(), totalTicketDiscountPrice);

                    returnOrderDetailTicketDiscount(orderDetailTicketDiscountDO, totalTicketDiscountPrice);

                    returnOrderTicketDiscount(orderTicketDiscountDO, totalTicketDiscountPrice);
                    // 票折全部退还完成
                    return;
                } else {
                    ticketDiscountRecordService.reduceUsedAmount(ticketDiscountRecordDO.getTicketDiscountNo(), allowReturnAmount);

                    returnOrderDetailTicketDiscount(orderDetailTicketDiscountDO, allowReturnAmount);

                    returnOrderTicketDiscount(orderTicketDiscountDO, allowReturnAmount);

                    totalTicketDiscountPrice = totalTicketDiscountPrice.subtract(allowReturnAmount);
                }
            }
        }
    }

    /**
     * 订单票折记录表 修改退还金额
     *
     * @param orderTicketDiscountDO 订单票折记录数据
     * @param returnAmount 退还金额
     */
    private void returnOrderTicketDiscount(OrderTicketDiscountDO orderTicketDiscountDO, BigDecimal returnAmount) {
        RefundOrderTicketDiscountRequest request = new RefundOrderTicketDiscountRequest();
        request.setId(orderTicketDiscountDO.getId());
        request.setRefundAmount(returnAmount);
        orderTicketDiscountService.refundOrderTicketDiscount(request);
    }

    /**
     * 订单明细票折信息 修改退还金额
     *
     * @param orderDetailTicketDiscountDO 原订单明细票折信息
     * @param returnAmount 退还金额
     */
    private void returnOrderDetailTicketDiscount(OrderDetailTicketDiscountDO orderDetailTicketDiscountDO, BigDecimal returnAmount) {
        RefundOrderDetailTicketDiscountRequest request = new RefundOrderDetailTicketDiscountRequest();
        request.setId(orderDetailTicketDiscountDO.getId());
        request.setRefundAmount(returnAmount);
        orderDetailTicketDiscountService.refundOrderDetailTicketDiscount(request);
    }

    /**
     * 退货单表数据组合
     *
     * @param orderReturnTypeEnum 退货单类型
     * @param orderDTO 订单信息
     * @param request 退货单请求数据
     * @return 退货单信息
     */
    private OrderReturnDO saveOrderReturn(OrderReturnTypeEnum orderReturnTypeEnum, OrderDTO orderDTO, OrderReturnApplyRequest request) {
        OrderReturnDO orderReturnDO = new OrderReturnDO();
        String orderReturnId = noService.gen(ORDER_RETURN_NO);
        orderReturnDO.setOrderId(orderDTO.getId());
        orderReturnDO.setOrderNo(orderDTO.getOrderNo());
        orderReturnDO.setReturnNo(orderReturnId);
        orderReturnDO.setBuyerEid(orderDTO.getBuyerEid());
        orderReturnDO.setBuyerEname(orderDTO.getBuyerEname());
        orderReturnDO.setSellerEid(orderDTO.getSellerEid());
        orderReturnDO.setSellerEname(orderDTO.getSellerEname());
        orderReturnDO.setDistributorEid(orderDTO.getDistributorEid());
        orderReturnDO.setDistributorEname(orderDTO.getDistributorEname());
        orderReturnDO.setDepartmentId(orderDTO.getDepartmentId());
        // 开票状态：1-待申请 2-已申请 3-已开票 4-申请驳回 5-已作废
        if (3 == orderDTO.getInvoiceStatus()) {
            // 发票状态：1-开票前 2-开票后
            orderReturnDO.setInvoiceStatus(OrderReturnInvoiceStatusEnum.AFTER_INVOICE.getCode());
        } else {
            orderReturnDO.setInvoiceStatus(OrderReturnInvoiceStatusEnum.BEFORE_INVOICE.getCode());
        }
        orderReturnDO.setContacterId(orderDTO.getContacterId());
        orderReturnDO.setContacterName(orderDTO.getContacterName());
        orderReturnDO.setErpPushStatus(OrderErpPushStatusEnum.NOT_PUSH.getCode());

        orderReturnDO.setReturnType(orderReturnTypeEnum.getCode());
        orderReturnDO.setReturnStatus(OrderReturnStatusEnum.ORDER_RETURN_PENDING.getCode());
        if (orderReturnTypeEnum.getCode().equals(OrderReturnTypeEnum.SELLER_RETURN_ORDER.getCode())) {
            orderReturnDO.setReturnStatus(OrderReturnStatusEnum.ORDER_RETURN_PASS.getCode());
            orderReturnDO.setReturnAuditTime(new Date());
        }
        orderReturnDO.setReturnSource(orderDTO.getOrderSource());
        orderReturnDO.setOrderReturnType(orderDTO.getOrderType());
        orderReturnDO.setRemark(request.getRemark());
        orderReturnDO.setOpUserId(request.getOpUserId());
        orderReturnDO.setOpTime(request.getOpTime());
        return orderReturnDO;
    }

    /**
     * 退货单将总退货金额回写到订单表
     *
     * @param orderId 订单id
     */
    private void operateOrderByReturn(Long orderId) {
        List<OrderReturnDTO> orderReturnList = orderReturnService.listPassedByOrderId(orderId);

        BigDecimal returnAmount = BigDecimal.ZERO;
        BigDecimal cashDiscountAmount = BigDecimal.ZERO;
        BigDecimal ticketDiscountAmount = BigDecimal.ZERO;
        for (OrderReturnDTO orderReturnDTO : orderReturnList) {
            returnAmount = returnAmount.add(orderReturnDTO.getReturnAmount());
            cashDiscountAmount = cashDiscountAmount.add(orderReturnDTO.getCashDiscountAmount());
            ticketDiscountAmount = ticketDiscountAmount.add(orderReturnDTO.getTicketDiscountAmount());
        }
        OrderDO orderDO = new OrderDO();
        orderDO.setId(orderId);
        orderDO.setReturnAmount(returnAmount);
        orderDO.setReturnCashDicountAmount(cashDiscountAmount);
        orderDO.setReturnTicketDiscountAmount(ticketDiscountAmount);
        log.info("operateOrderByReturn, orderDO :[{}]", JSONUtil.toJsonStr(orderDO));
        orderService.updateById(orderDO);
    }
}

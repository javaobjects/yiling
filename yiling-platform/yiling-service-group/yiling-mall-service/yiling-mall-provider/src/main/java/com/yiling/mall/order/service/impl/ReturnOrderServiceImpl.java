package com.yiling.mall.order.service.impl;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.inventory.dto.request.AddOrSubtractQtyRequest;
import com.yiling.mall.order.dto.request.OrderCouponUseReturnRequest;
import com.yiling.mall.order.dto.request.RefundOrderRequest;
import com.yiling.mall.order.service.OrderCouponUseService;
import com.yiling.mall.order.service.OrderRefundService;
import com.yiling.mall.order.service.ReturnOrderService;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;
import com.yiling.marketing.promotion.dto.request.PromotionUpdateBuyRecordRequest;
import com.yiling.marketing.promotion.dto.request.PromotionUpdateDetailRequest;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderCouponUseApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderPromotionActivityApi;
import com.yiling.order.order.api.OrderReturnApi;
import com.yiling.order.order.api.OrderReturnDetailApi;
import com.yiling.order.order.dto.OrderCouponUseDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderPromotionActivityDTO;
import com.yiling.order.order.dto.OrderReturnDTO;
import com.yiling.order.order.dto.OrderReturnDetailDTO;
import com.yiling.order.order.dto.request.B2BOrderReturnApplyRequest;
import com.yiling.order.order.dto.request.B2BOrderReturnDetailApplyRequest;
import com.yiling.order.order.dto.request.B2BOrderReturnDetailBatchApplyRequest;
import com.yiling.order.order.dto.request.OrderDeliveryRequest;
import com.yiling.order.order.dto.request.OrderDetailRequest;
import com.yiling.order.order.dto.request.OrderReturnApplyRequest;
import com.yiling.order.order.dto.request.OrderReturnVerifyRequest;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderReturnTypeEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.order.order.enums.RefundSourceEnum;
import com.yiling.order.order.enums.ReturnSourceEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.request.RefundPaymentDaysAmountRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author tingwei.chen
 * @date 2021-07-19
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReturnOrderServiceImpl implements ReturnOrderService {

    @DubboReference
    OrderReturnApi orderReturnApi;
    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    OrderCouponUseApi orderCouponUseApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    InventoryApi inventoryApi;
    @DubboReference
    OrderDetailApi orderDetailApi;
    @DubboReference
    OrderReturnDetailApi orderReturnDetailApi;
    @DubboReference
    PromotionActivityApi promotionActivityApi;
    @DubboReference
    OrderPromotionActivityApi orderPromotionActivityApi;


    private final OrderRefundService orderRefundService;

    private final OrderCouponUseService orderCouponUseService;

    /**
     * 退货单请求参数的处理
     *
     * @param request 退货单申请请求参数
     */
    private void operateOrderReturnRequest(OrderReturnApplyRequest request) {
        log.info("createOrderReturn operateOrderReturnRequest start request:[{}]", JSONUtil.toJsonStr(request));
        List<OrderDetailRequest> detailList = new ArrayList<>();
        List<OrderDetailRequest> orderDetailList = request.getOrderDetailList();
        for (OrderDetailRequest orderDetailRequest : orderDetailList) {
            List<OrderDeliveryRequest> orderDeliveryList = orderDetailRequest.getOrderDeliveryList();
            if (CollUtil.isEmpty(orderDeliveryList)) {
                continue;
            }
            List<OrderDeliveryRequest> deliveryRequestList = orderDeliveryList.stream().filter(e -> null != e.getReturnQuantity() && e.getReturnQuantity() > 0).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(deliveryRequestList)) {
                orderDetailRequest.setOrderDeliveryList(deliveryRequestList);
                detailList.add(orderDetailRequest);
            }
        }
        request.setOrderDetailList(detailList);
        log.info("createOrderReturn operateOrderReturnRequest end request:[{}]", JSONUtil.toJsonStr(request));
    }

    /**
     * POP破损退货
     *
     * @param orderReturnApplyRequest 退货请求数据
     * @param fromWhere 来源（1.采购商申请，2.收货）
     * @param orderDTO 订单信息
     * @return 成功/失败
     */
    @Override
    @GlobalTransactional
    public Boolean damageOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest, Integer fromWhere, OrderDTO orderDTO) {
        operateOrderReturnRequest(orderReturnApplyRequest);
        if (1 == fromWhere) {
            // 手动申请破损退货单需要将收货数量改变
            orderReturnApi.beforeDamageOrderReturn(orderReturnApplyRequest);
        }
        return orderReturnApi.damageOrderReturn(orderReturnApplyRequest, orderDTO);
    }

    /**
     * POP采购商退货
     *
     * @param orderReturnApplyRequest 退货请求数据
     * @return 成功/失败
     */
    @Override
    @GlobalTransactional
    public Boolean purchaseApplyReturnOrder(OrderReturnApplyRequest orderReturnApplyRequest) {
        // 首先精简一下退货单申请数据
        operateOrderReturnRequest(orderReturnApplyRequest);
        orderReturnApi.beforeDamageOrderReturn(orderReturnApplyRequest);
        OrderDTO orderDTO = orderApi.getOrderInfo(orderReturnApplyRequest.getOrderId());
        // 只有以岭的采购商退货才需要已经开票
        return orderReturnApi.purchaseApplyReturnOrder(orderReturnApplyRequest, orderDTO);
    }

    /**
     * 供应商退货
     *
     * @param orderReturnApplyRequest 退货请求数据
     * @param orderDTO 订单信息
     * @return 成功/失败
     */
    @Override
    @GlobalTransactional
    public Boolean supplierApplyOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest, OrderDTO orderDTO) {
        OrderReturnDTO orderReturnDTO = orderReturnApi.supplierApplyOrderReturn(orderReturnApplyRequest, orderDTO);
        // 供应商退货需要解除冻结库存
        removePOPFrozenInventory(orderReturnApplyRequest);

        if (null != orderReturnDTO && PaymentMethodEnum.getByCode(orderDTO.getPaymentMethod().longValue()) == PaymentMethodEnum.PAYMENT_DAYS &&  PaymentStatusEnum.getByCode(orderDTO.getPaymentStatus()) == PaymentStatusEnum.UNPAID) {
            return returnPaymentDaysAccount(orderReturnApplyRequest, orderReturnDTO);
        }
        return null != orderReturnDTO;
    }

    /**
     * POP解除冻结库存
     *
     * @param orderReturnApplyRequest POP退货请求数据
     * @return 成功/失败
     */
    private boolean removePOPFrozenInventory(OrderReturnApplyRequest orderReturnApplyRequest) {
        List<OrderDetailDTO> detailDTOList = orderDetailApi.getOrderDetailInfo(orderReturnApplyRequest.getOrderId());
        Map<Long, OrderDetailDTO> orderDetailDTOMap = detailDTOList.stream().collect(Collectors.toMap(OrderDetailDTO::getId, e -> e));
        List<OrderDetailRequest> orderDetailList = orderReturnApplyRequest.getOrderDetailList();
        for (OrderDetailRequest orderDetailRequest : orderDetailList) {
            OrderDetailDTO orderDetailDTO = orderDetailDTOMap.get(orderDetailRequest.getDetailId());
            Long goodsSkuId = orderDetailDTO.getGoodsSkuId();
            List<OrderDeliveryRequest> orderDeliveryList = orderDetailRequest.getOrderDeliveryList();
            OrderDeliveryRequest orderDeliveryRequest = orderDeliveryList.get(0);
            AddOrSubtractQtyRequest request = new AddOrSubtractQtyRequest();
            request.setSkuId(goodsSkuId);
            request.setFrozenQty(orderDeliveryRequest.getReturnQuantity().longValue());
            request.setOrderNo(orderDetailDTO.getOrderNo());
            request.setOpUserId(orderReturnApplyRequest.getOpUserId());
            request.setOpTime(orderReturnApplyRequest.getOpTime());

            log.info("removePOPFrozenInventory,subtractFrozenQty:[{}]", request);
            inventoryApi.subtractFrozenQty(request);
        }
        return true;
    }

    /**
     * B2B解除冻结库存
     *
     * @param request B2B退货请求数据
     * @return 成功/失败
     */
    private boolean removeB2BFrozenInventory(OrderReturnApplyRequest request) {
        List<OrderDetailDTO> detailDTOList = orderDetailApi.getOrderDetailInfo(request.getOrderId());
        Map<Long, OrderDetailDTO> orderDetailDTOMap = detailDTOList.stream().collect(Collectors.toMap(OrderDetailDTO::getId, e -> e));
        List<OrderDetailRequest> orderDetailList = request.getOrderDetailList();
        for (OrderDetailRequest orderDetailRequest : orderDetailList) {
            OrderDetailDTO orderDetailDTO = orderDetailDTOMap.get(orderDetailRequest.getDetailId());
            Long goodsSkuId = orderDetailDTO.getGoodsSkuId();
            List<OrderDeliveryRequest> orderDeliveryList = orderDetailRequest.getOrderDeliveryList();
            OrderDeliveryRequest batchApplyRequest = orderDeliveryList.get(0);
            AddOrSubtractQtyRequest addOrSubtractQtyRequest = new AddOrSubtractQtyRequest();
            addOrSubtractQtyRequest.setSkuId(goodsSkuId);
            addOrSubtractQtyRequest.setFrozenQty(batchApplyRequest.getReturnQuantity().longValue());
            addOrSubtractQtyRequest.setOrderNo(orderDetailDTO.getOrderNo());
            addOrSubtractQtyRequest.setOpUserId(request.getOpUserId());
            addOrSubtractQtyRequest.setOpTime(request.getOpTime());

            log.info("removeB2BFrozenInventory,subtractFrozenQty:[{}]", addOrSubtractQtyRequest);
            inventoryApi.subtractFrozenQty(addOrSubtractQtyRequest);
        }
        return true;
    }

    /**
     * 退还账期--供应商退货和采购商退货审核通过
     *
     * @param orderReturnApplyRequest 退货请求数据
     * @return 成功/失败
     */
    private boolean returnPaymentDaysAccount(OrderReturnApplyRequest orderReturnApplyRequest, OrderReturnDTO orderReturnDTO) {
        log.info("supplierApplyOrderReturn returnPaymentDaysAccount, orderReturnApplyRequest :[{}], orderReturnDTO :[{}]", orderReturnApplyRequest, orderReturnDTO);

        BigDecimal refundAmount = orderReturnDTO.getReturnAmount().subtract(orderReturnDTO.getCashDiscountAmount()).subtract(orderReturnDTO.getTicketDiscountAmount());
        RefundPaymentDaysAmountRequest refundPaymentDaysAmountRequest = new RefundPaymentDaysAmountRequest();
        refundPaymentDaysAmountRequest.setOrderId(orderReturnApplyRequest.getOrderId());
        refundPaymentDaysAmountRequest.setRefundAmount(refundAmount);
        refundPaymentDaysAmountRequest.setOpUserId(orderReturnApplyRequest.getOpUserId());
        refundPaymentDaysAmountRequest.setReturnNo(orderReturnDTO.getReturnNo());
        refundPaymentDaysAmountRequest.setPlatformEnum(PlatformEnum.POP);
        return paymentDaysAccountApi.refund(refundPaymentDaysAmountRequest);
    }

    /**
     * POP退货审核
     *
     * @param orderReturnApplyRequest POP退货审核请求数据
     * @return 成功/失败
     */
    @Override
    @GlobalTransactional
    public Boolean checkOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest) {
        OrderDTO orderDTO = orderApi.getOrderInfo(orderReturnApplyRequest.getOrderId());
        boolean isYiLing = enterpriseApi.isYilingSubEid(orderDTO.getSellerEid());
        List<Long> longList = enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.INDUSTRY_DIRECT);
        boolean contains = longList.contains(orderDTO.getSellerEid());
        isYiLing = isYiLing || contains;
        OrderReturnDTO orderReturnDTO = orderReturnApi.checkOrderReturn(orderReturnApplyRequest, isYiLing);
        return null != orderReturnDTO;
    }

    @Override
    @GlobalTransactional
    public Boolean deliverOrderReturn(OrderReturnApplyRequest request) {
        OrderDTO orderDTO = orderApi.getOrderInfo(request.getOrderId());
        // 供应商退货 需要退款
        OrderReturnDTO orderReturnDTO = orderReturnApi.deliverOrderReturn(request);

        // 退优惠券
        couponReturn(orderDTO.getId(), request.getOpUserId(), request.getOpTime());

        // 退活动商品数量限购额度
        startReturnPromotionGoodsLimit(orderReturnDTO.getId(), orderReturnDTO.getOrderId());

        // 供应商退货需要解除冻结库存
        removeB2BFrozenInventory(request);

        //退款
        return returnAmount(orderDTO, orderReturnDTO, request.getOpUserId(), request.getOpTime());
    }

    @Override
    @GlobalTransactional
    public Boolean applyOrderReturn(OrderReturnApplyRequest request) {

        validUnionActivityQuantity(request);

        return orderReturnApi.applyOrderReturn(request);
    }

    /**
     * 校验组合商品是否满足组合套装
     *
     * @param request
     */
    private void validUnionActivityQuantity(OrderReturnApplyRequest request) {
        OrderPromotionActivityDTO activityDTO = orderPromotionActivityApi.getOneByOrderIds(request.getOrderId());
        if (activityDTO != null && PromotionActivityTypeEnum.COMBINATION == PromotionActivityTypeEnum.getByCode(activityDTO.getActivityType())) {
            List<PromotionGoodsLimitDTO> promotionGoodsLimitList = promotionActivityApi.queryGoodsByActivityId(activityDTO.getActivityId());
            //促销的组合包允许购买数量
            Map<Long, Integer> promotionGoodsMap = promotionGoodsLimitList.stream().collect(Collectors.toMap(PromotionGoodsLimitDTO::getGoodsSkuId, PromotionGoodsLimitDTO::getAllowBuyCount, (k1, k2) -> k1));

            //已审核退货数
            Map<Long, Integer> returnMap = new HashMap<>();
            List<OrderReturnDTO> orderReturnList = orderReturnApi.listByOrderId(request.getOrderId());
            if (CollectionUtil.isNotEmpty(orderReturnList)) {
                List<Long> returnIds = orderReturnList.stream().map(o -> o.getId()).collect(Collectors.toList());
                List<OrderReturnDetailDTO> orderReturnDetailList = orderReturnDetailApi.getOrderReturnDetailByReturnIds(returnIds);
                returnMap = orderReturnDetailList.stream().collect(Collectors.groupingBy(OrderReturnDetailDTO::getGoodsSkuId, Collectors.summingInt(OrderReturnDetailDTO::getReturnQuantity)));
            }
            //入参数量
            List<OrderDetailRequest> orderReturnDetailList = request.getOrderDetailList();
            Map<Long, Integer> requestMap = orderReturnDetailList.stream().collect(Collectors.toMap(o -> o.getGoodsSkuId(), o -> o.getOrderDeliveryList().stream().mapToInt(OrderDeliveryRequest::getReturnQuantity).sum()));

            //购买数量，和商品名称
            List<OrderDetailDTO> orderDetailList = orderDetailApi.getOrderDetailInfo(request.getOrderId());
            Map<Long, Integer> orderDetailMap = orderDetailList.stream().collect(Collectors.toMap(OrderDetailDTO::getGoodsSkuId, OrderDetailDTO::getGoodsQuantity));
            Map<Long, String> goodsNameMap = orderDetailList.stream().collect(Collectors.toMap(OrderDetailDTO::getGoodsSkuId, OrderDetailDTO::getGoodsName));


            //提示语句
            StringBuilder message = new StringBuilder();
            //判断条件集合
            Integer condition = 0;
            //倍数信息
            Integer multiple = 0;
            //标记是否第一次进入，保存倍数
            Boolean flag = true;
            for (Map.Entry<Long, Integer> entry : promotionGoodsMap.entrySet()) {
                //输入数量
                Integer requestQuantity = requestMap.getOrDefault(entry.getKey(), 0);
                //购买数量
                Integer buyQuantity = orderDetailMap.getOrDefault(entry.getKey(), 0);
                //退货数量
                Integer returnQuantity = returnMap.getOrDefault(entry.getKey(), 0);
                //组合包数量
                Integer promotionQuantity = entry.getValue();
                //商品名称
                String name = goodsNameMap.get(entry.getKey());
                if (requestQuantity == 0) {
                    //输入数量为0时，退货数量等于购买数量。符合组合包数据
                    if (returnQuantity == buyQuantity) {
                        condition = condition + 1;
                    } else {
                        message.append(name + "退货数量不符合组合数:" + promotionQuantity);
                    }
                } else {
                    //输入数量不为0时，输入数量 + 退货数量 是组合包的倍数，并且倍数要保持一直
                    //取余
                    int i = (returnQuantity + requestQuantity) % promotionQuantity;
                    //取整
                    int number = (returnQuantity + requestQuantity) / promotionQuantity;
                    //输入取余
                    int requestRemain = requestQuantity % promotionQuantity;
                    //输入取整
                    int requestNum = requestQuantity / promotionQuantity;

                    if ((i != 0 && requestRemain != 0) || number == 0) {
                        message.append(name + "退货数量不符合组合数:" + promotionQuantity);
                    } else {

                        if (flag) {
                            if (requestRemain == 0) {
                                multiple = requestNum;
                            } else {
                                multiple = number;
                            }
                            flag = false;
                            condition = condition + 1;
                        } else {
                            if (requestRemain == 0) {
                                if (requestNum != multiple) {
                                    //倍数不相同
                                    message.append(name + "退货数量不符合组合数:" + promotionQuantity);
                                } else {
                                    condition = condition + 1;
                                }
                            } else {
                                if (number != multiple) {
                                    //倍数不相同
                                    message.append(name + "退货数量不符合组合数:" + promotionQuantity);
                                } else {
                                    condition = condition + 1;
                                }
                            }

                        }
                    }
                }
            }
            if (condition != promotionGoodsMap.size()) {
                String failMsg = MessageFormat.format(OrderErrorCode.ORDER_RETURN_UNION_ACTIVITY_QUANTITY_ERROR.getMessage(), message.toString());
                throw new BusinessException(OrderErrorCode.ORDER_RETURN_UNION_ACTIVITY_QUANTITY_ERROR, failMsg);
            }

        }
    }

    @Override
    @GlobalTransactional
    public Boolean verifyOrderReturn(OrderReturnVerifyRequest request) {
        // 审核通过后 需要退款
        OrderReturnDTO orderReturnDTO = orderReturnApi.verifyOrderReturn(request);
        OrderDTO orderDTO = orderApi.getOrderInfo(orderReturnDTO.getOrderId());
        if (0 == request.getIsSuccess()) {
            // 退优惠券
            couponReturn(orderDTO.getId(), request.getOpUserId(), request.getOpTime());

            // 退活动商品数量限购额度
            startReturnPromotionGoodsLimit(orderReturnDTO.getId(), orderReturnDTO.getOrderId());

            // 退款
            return returnAmount(orderDTO, orderReturnDTO, request.getOpUserId(), request.getOpTime());
        } else {
            return null != orderReturnDTO;
        }
    }

    @Override
    public Boolean applySaOrderReturn(B2BOrderReturnApplyRequest request) {
        log.info("applySaOrderReturn, request:[{}]", request);
        OrderDTO orderDTO = orderApi.getOrderInfo(request.getOrderId());
        if (OrderTypeEnum.B2B.getCode().equals(orderDTO.getOrderType())) {
            request.setReturnSource(ReturnSourceEnum.SA.getCode());
            OrderReturnApplyRequest requests = buildRequest(request);
            return this.applyOrderReturn(requests);
        } else if (OrderTypeEnum.POP.getCode().equals(orderDTO.getOrderType())) {
            OrderReturnApplyRequest applyRequest = buildPopApplySaOrderReturnRequest(request, orderDTO);
            return this.purchaseApplyReturnOrder(applyRequest);
        }
        return true;
    }

    /**
     * 构建申请退货单请求数据
     *
     * @param request 请求数据
     * @return 构造后的请求数据
     */
    private OrderReturnApplyRequest buildRequest(B2BOrderReturnApplyRequest request) {
        OrderReturnApplyRequest returnApplyRequest = PojoUtils.map(request, OrderReturnApplyRequest.class);
        returnApplyRequest.setReturnType(OrderReturnTypeEnum.BUYER_RETURN_ORDER.getCode());
        List<B2BOrderReturnDetailApplyRequest> orderReturnDetailList = request.getOrderReturnDetailList();
        List<OrderDetailRequest> orderDetailRequestList = new ArrayList<>();
        for (B2BOrderReturnDetailApplyRequest b2BOrderReturnDetailApplyRequest : orderReturnDetailList) {
            OrderDetailRequest orderDetailRequest = PojoUtils.map(b2BOrderReturnDetailApplyRequest, OrderDetailRequest.class);
            List<B2BOrderReturnDetailBatchApplyRequest> orderReturnDetailBatchList = b2BOrderReturnDetailApplyRequest.getOrderReturnDetailBatchList();
            List<OrderDeliveryRequest> orderDeliveryRequests = PojoUtils.map(orderReturnDetailBatchList, OrderDeliveryRequest.class);
            orderDetailRequest.setOrderDeliveryList(orderDeliveryRequests);
            orderDetailRequestList.add(orderDetailRequest);
        }
        returnApplyRequest.setOrderDetailList(orderDetailRequestList);
        return returnApplyRequest;
    }

    /**
     * 根据退货单明细退还秒杀数量限制--排除没有参与活动的订单明细
     *
     * @param returnId 退货单id
     * @param orderId 订单id
     */
    private void startReturnPromotionGoodsLimit(Long returnId, Long orderId) {
        List<OrderReturnDetailDTO> returnDetailList = orderReturnDetailApi.getOrderReturnDetailByReturnId(returnId);
        List<OrderDetailDTO> orderDetailDTOList = orderDetailApi.getOrderDetailInfo(orderId);

        List<OrderDetailDTO> dtoList = orderDetailDTOList.stream().filter(e -> PromotionActivityTypeEnum.getByCode(e.getPromotionActivityType()) == PromotionActivityTypeEnum.LIMIT || PromotionActivityTypeEnum.getByCode(e.getPromotionActivityType()) == PromotionActivityTypeEnum.SPECIAL).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(dtoList)) {
            returnPromotionGoodsLimit(returnDetailList, dtoList, orderId);
        }
    }

    /**
     * 根据退货单明细和订单明细退还秒杀数量限制
     *
     * @param returnDetailList 退货单明细
     * @param orderDetailDTOList 秒杀特价的订单明细
     * @param orderId 订单id
     */
    private void returnPromotionGoodsLimit(List<OrderReturnDetailDTO> returnDetailList, List<OrderDetailDTO> orderDetailDTOList, Long orderId) {
        log.info("OrderReturn returnPromotionGoodsLimit start, returnDetailList:[{}], orderDetailDTOList:[{}]", returnDetailList, orderDetailDTOList);
        PromotionUpdateBuyRecordRequest request = new PromotionUpdateBuyRecordRequest().setOrderId(orderId);

        Map<Long, OrderDetailDTO> detailMap = orderDetailDTOList.stream().collect(Collectors.toMap(OrderDetailDTO::getId, e -> e));
        log.info("OrderReturn returnPromotionGoodsLimit detailMap:[{}]", detailMap);

        List<PromotionUpdateDetailRequest> detailList = new ArrayList<>();
        for (OrderReturnDetailDTO returnDetailDTO : returnDetailList) {
            OrderDetailDTO detailDTO = detailMap.get(returnDetailDTO.getDetailId());

            if (PromotionActivityTypeEnum.getByCode(detailDTO.getPromotionActivityType()) != PromotionActivityTypeEnum.NORMAL) {
                PromotionUpdateDetailRequest detailRequest = new PromotionUpdateDetailRequest().setGoodsId(detailDTO.getDistributorGoodsId()).setQuantity(returnDetailDTO.getReturnQuantity());
                detailList.add(detailRequest);
            }
        }

        if (CollUtil.isNotEmpty(detailList)) {
            request.setDetailList(detailList);
            log.info("OrderReturn returnPromotionGoodsLimit updateBuyRecordQuantity, request:[{}]", request);
            promotionActivityApi.updateBuyRecordQuantity(request);
        }
    }

    /**
     * B2B申请退货单的请求参数  转换成  POP申请退货单请求参数
     *
     * @param request B2B申请退货单请求参数
     * @param orderDTO 订单信息
     * @return POP申请退货单请求参数
     */
    private OrderReturnApplyRequest buildPopApplySaOrderReturnRequest(B2BOrderReturnApplyRequest request, OrderDTO orderDTO) {
        OrderReturnApplyRequest popRequest = new OrderReturnApplyRequest();
        popRequest.setOrderId(orderDTO.getId());
        popRequest.setReturnType(OrderReturnTypeEnum.BUYER_RETURN_ORDER.getCode());
        popRequest.setOpUserId(request.getOpUserId());
        List<OrderDetailRequest> orderDetailList = new ArrayList<>();
        for (B2BOrderReturnDetailApplyRequest returnDetailApplyRequest : request.getOrderReturnDetailList()) {
            OrderDetailRequest orderDetailRequest = new OrderDetailRequest();
            orderDetailRequest.setDetailId(returnDetailApplyRequest.getDetailId());
            orderDetailRequest.setGoodsId(returnDetailApplyRequest.getGoodsId());
            List<OrderDeliveryRequest> orderDeliveryList = new ArrayList<>();
            for (B2BOrderReturnDetailBatchApplyRequest batchApplyRequest : returnDetailApplyRequest.getOrderReturnDetailBatchList()) {
                OrderDeliveryRequest deliveryRequest = new OrderDeliveryRequest();
                deliveryRequest.setBatchNo(batchApplyRequest.getBatchNo());
                deliveryRequest.setReturnQuantity(batchApplyRequest.getReturnQuantity());
                orderDeliveryList.add(deliveryRequest);
            }
            orderDetailRequest.setOrderDeliveryList(orderDeliveryList);
            orderDetailList.add(orderDetailRequest);
        }
        popRequest.setOrderDetailList(orderDetailList);
        return popRequest;
    }

    /**
     * 退款（账期/线上支付）
     *
     * @param orderDTO 订单信息
     * @param orderReturnDTO 退货单信息
     * @param currentUserId 当前操作人id
     * @param opTime 操作时间
     * @return 成功/失败
     */
    private boolean returnAmount(OrderDTO orderDTO, OrderReturnDTO orderReturnDTO, Long currentUserId, Date opTime) {
        BigDecimal refundAmount = orderReturnDTO.getReturnAmount()
                .subtract(orderReturnDTO.getPlatformCouponDiscountAmount())
                .subtract(orderReturnDTO.getCouponDiscountAmount())
                .subtract(orderReturnDTO.getShopPaymentDiscountAmount())
                .subtract(orderReturnDTO.getPlatformPaymentDiscountAmount())
                .subtract(orderReturnDTO.getPresaleDiscountAmount());
        //支付方式：1-线下支付 2-账期 3-预付款 4-在线支付
        if (PaymentMethodEnum.PAYMENT_DAYS.getCode() == orderDTO.getPaymentMethod().longValue()&& PaymentStatusEnum.getByCode(orderDTO.getPaymentStatus()) == PaymentStatusEnum.UNPAID) {
            // 1.如果是账期支付，退还账期
            RefundPaymentDaysAmountRequest refundPaymentDaysAmountRequest = new RefundPaymentDaysAmountRequest();
            refundPaymentDaysAmountRequest.setOrderId(orderReturnDTO.getOrderId());
            refundPaymentDaysAmountRequest.setRefundAmount(refundAmount);
            refundPaymentDaysAmountRequest.setOpUserId(currentUserId);
            refundPaymentDaysAmountRequest.setReturnNo(orderReturnDTO.getReturnNo());
            refundPaymentDaysAmountRequest.setPlatformEnum(PlatformEnum.B2B);

            log.info("returnAmount refundPaymentDaysAmount, request :[{}]", refundPaymentDaysAmountRequest);
            return paymentDaysAccountApi.refund(refundPaymentDaysAmountRequest);
        } else if (PaymentMethodEnum.ONLINE.getCode() == orderDTO.getPaymentMethod().longValue() && PaymentStatusEnum.PAID.getCode().equals(orderDTO.getPaymentStatus())) {
            RefundOrderRequest request = new RefundOrderRequest();
            request.setBuyerEid(orderDTO.getBuyerEid());
            request.setSellerEid(orderDTO.getSellerEid());
            request.setTotalAmount(orderDTO.getTotalAmount());
            request.setPaymentAmount(orderDTO.getPaymentAmount());
            request.setOrderId(orderDTO.getId())
                    .setOrderNo(orderDTO.getOrderNo())
                    .setReturnId(orderReturnDTO.getId())
                    .setReturnNo(orderReturnDTO.getReturnNo())
                    .setRefundType(2)
                    .setRefundAmount(refundAmount);
            request.setOpUserId(currentUserId);
            request.setOpTime(opTime);
            request.setRefundSource(RefundSourceEnum.NORMAL.getCode());
            if (OrderReturnTypeEnum.SELLER_RETURN_ORDER.getCode().equals(orderReturnDTO.getReturnType())) {
                request.setRefundType(3);
            }
            log.info("returnAmount refundOrder, request :[{}]", request);
            return orderRefundService.refundOrder(request);
        }
        // 3.如果是线下支付，线下处理
        return true;
    }

    /**
     * 退优惠券
     *
     * @param orderId 订单id
     * @param opUserId 操作人id
     * @param opTime 操作时间
     */
    private void couponReturn(Long orderId, Long opUserId, Date opTime) {
        log.info("couponReturn, orderId :[{}]", orderId);
        Long returnCouponId = returnCoupons(orderId);
        Long returnPlateCouponId = returnPlateCoupons(orderId);
        List<Long> couponIdList = new ArrayList<>();
        if (null != returnCouponId && returnCouponId > 0) {
            couponIdList.add(returnCouponId);
        }
        if (null != returnPlateCouponId && returnPlateCouponId > 0) {
            couponIdList.add(returnPlateCouponId);
        }
        if (couponIdList.size() < 1) {
            return;
        }
        OrderCouponUseReturnRequest request = new OrderCouponUseReturnRequest();
        request.setCouponIdList(couponIdList);
        request.setOpUserId(opUserId);
        request.setOpTime(opTime);

        log.info("couponReturn orderReturnCoupon, request :[{}]", request);
        orderCouponUseService.orderReturnCoupon(request);
    }

    /**
     * 商家退的优惠券id
     *
     * @param orderId 订单id
     * @return 优惠券id
     */
    private Long returnCoupons(Long orderId) {
        // 1.退还商家优惠券
        List<OrderCouponUseDTO> orderCouponUseDOList = orderCouponUseApi.listOrderCoupon(orderId, 2);
        if (orderCouponUseDOList.size() <= 0) {
            return null;
        }
        List<OrderDetailChangeDTO> orderDetailChangeDOS = orderDetailChangeApi.listByOrderId(orderId);
        OrderCouponUseDTO orderCouponUseDO = orderCouponUseDOList.get(0);

        log.info("returnCoupons, orderDetailChangeDOS :[{}], orderCouponUseDO :[{}]", orderDetailChangeDOS, orderCouponUseDO);
        if (!isReturnCoupon(orderDetailChangeDOS, orderCouponUseDO)) {
            return null;
        }
        return orderCouponUseDO.getCouponId();
    }

    /**
     * 平台退的优惠券id
     *
     * @param orderId 订单id
     * @return 优惠券id
     */
    private Long returnPlateCoupons(Long orderId) {
        // 2.退还平台券
        List<OrderCouponUseDTO> orderPlateCouponUseDOList = orderCouponUseApi.listOrderCoupon(orderId, 1);
        if (orderPlateCouponUseDOList.size() <= 0) {
            return null;
        }
        OrderCouponUseDTO orderPlateCouponUseDO = orderPlateCouponUseDOList.get(0);
        // 查询这个平台优惠券有哪些订单使用
        List<Long> longList = new ArrayList<>();
        longList.add(orderPlateCouponUseDO.getCouponId());
        List<OrderCouponUseDTO> orderCouponUseDOS = orderCouponUseApi.listByCouponIdList(longList);
        List<Long> orderIdList = orderCouponUseDOS.stream().map(OrderCouponUseDTO::getOrderId).collect(Collectors.toList());
        List<OrderDetailChangeDTO> orderDetailChangeDOList = orderDetailChangeApi.listByOrderIds(orderIdList);

        log.info("returnPlateCoupons, orderDetailChangeDOList :[{}], orderPlateCouponUseDO :[{}]", orderDetailChangeDOList, orderPlateCouponUseDO);
        if (!isReturnPlateCoupon(orderDetailChangeDOList, orderPlateCouponUseDO)) {
            return null;
        }
        return orderPlateCouponUseDO.getCouponId();
    }

    /**
     * 判断是否退优惠券-商家券
     *
     * @param orderDetailChangeDOList 订单明细修改表集合
     * @param orderCouponUseDO 订单使用优惠券表
     * @return 是否允许退
     */
    private boolean isReturnCoupon(List<OrderDetailChangeDTO> orderDetailChangeDOList, OrderCouponUseDTO orderCouponUseDO) {
        BigDecimal returnCouponDiscountAmount = BigDecimal.ZERO;
        for (OrderDetailChangeDTO orderDetailChangeDO : orderDetailChangeDOList) {
            returnCouponDiscountAmount = returnCouponDiscountAmount.add(orderDetailChangeDO.getSellerCouponDiscountAmount());
            returnCouponDiscountAmount = returnCouponDiscountAmount.add(orderDetailChangeDO.getReturnCouponDiscountAmount());
        }
        return returnCouponDiscountAmount.compareTo(orderCouponUseDO.getAmount()) == 0;
    }

    /**
     * 判断是否退优惠券-平台券
     *
     * @param orderDetailChangeDOList 订单明细修改表集合
     * @param orderCouponUseDO 订单使用优惠券表
     * @return 是否允许退
     */
    private boolean isReturnPlateCoupon(List<OrderDetailChangeDTO> orderDetailChangeDOList, OrderCouponUseDTO orderCouponUseDO) {
        BigDecimal returnCouponDiscountAmount = BigDecimal.ZERO;
        for (OrderDetailChangeDTO orderDetailChangeDO : orderDetailChangeDOList) {
            returnCouponDiscountAmount = returnCouponDiscountAmount.add(orderDetailChangeDO.getSellerPlatformCouponDiscountAmount());
            returnCouponDiscountAmount = returnCouponDiscountAmount.add(orderDetailChangeDO.getReturnPlatformCouponDiscountAmount());
        }
        return returnCouponDiscountAmount.compareTo(orderCouponUseDO.getAmount()) == 0;
    }
}

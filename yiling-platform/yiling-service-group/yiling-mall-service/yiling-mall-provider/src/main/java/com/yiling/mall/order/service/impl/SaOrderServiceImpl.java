package com.yiling.mall.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.order.bo.CalOrderDiscountContextBO;
import com.yiling.mall.order.bo.OrderSubmitBO;
import com.yiling.mall.order.bo.SplitOrderEnum;
import com.yiling.mall.order.dto.request.OrderConfirmRequest;
import com.yiling.mall.order.dto.request.PopOrderConfirmRequest;
import com.yiling.mall.order.event.CreateOrderEvent;
import com.yiling.mall.order.handler.CouponDiscountProcessHandler;
import com.yiling.mall.order.handler.DiscountHandlerChain;
import com.yiling.mall.order.handler.GiftDiscountProcessHandler;
import com.yiling.mall.order.service.OrderDeliveryAndReceiveProcessService;
import com.yiling.mall.order.service.SaOrderService;
import com.yiling.mall.payment.api.PaymentApi;
import com.yiling.mall.payment.dto.request.PaymentRequest;
import com.yiling.marketing.promotion.dto.PromotionReduceStockDTO;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderB2BApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.request.B2bOrderConfirmRequest;
import com.yiling.order.order.dto.request.CreateOrderDetailRequest;
import com.yiling.order.order.dto.request.CreateOrderRequest;
import com.yiling.order.order.dto.request.UpdateOrderRemarkRequest;
import com.yiling.order.order.enums.CustomerConfirmEnum;
import com.yiling.order.order.enums.OrderAuditStatusEnum;
import com.yiling.order.order.enums.OrderCategoryEnum;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderGoodsTypeEnum;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.PaymentDaysAccountDTO;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * 销售助手订单订单业务逻辑数据
 * @author zhigang.guo
 * @date: 2022/3/3
 */
@Service
@Slf4j
public class SaOrderServiceImpl extends OrderSubmitServiceImpl implements SaOrderService {
    @DubboReference
    PaymentDaysAccountApi                    paymentDaysAccountApi;
    @DubboReference
    OrderDetailApi                           orderDetailApi;
    @DubboReference
    OrderApi                                 orderApi;
    @DubboReference
    OrderB2BApi                              orderB2BApi;
    @DubboReference
    PaymentApi                               paymentApi;
    @Lazy
    @Autowired
    SaOrderServiceImpl                       _this;
    @Autowired
    OrderDeliveryAndReceiveProcessService    orderDeliveryAndReceiveProcessService;
    @Autowired
    CouponDiscountProcessHandler             couponDiscountProcessHandler;
    @Autowired
    GiftDiscountProcessHandler               giftDiscountProcessHandler;


    /**
     * 获取订单对应的店铺账期上浮值
     * @param orderInfo 订单基本信息
     * @return 店铺设置的账期上浮值
     */
    private BigDecimal getShopUpPoint(B2bOrderConfirmRequest orderInfo) {

        // 如果是账期支付，需要上浮价格
        if (orderInfo.getPaymentMethod() != null && PaymentMethodEnum.PAYMENT_DAYS == PaymentMethodEnum.getByCode(orderInfo.getPaymentMethod().longValue())) {
            PaymentDaysAccountDTO accountDTO = paymentDaysAccountApi.getByCustomerEid(orderInfo.getSellerEid(),orderInfo.getBuyerEid());

            if (accountDTO == null) {

                throw new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_NOT_EXISTS);
            }

            if (accountDTO.getUpPoint() != null) {
                BigDecimal upPoint = new BigDecimal(1);

                upPoint = upPoint.add(NumberUtil.div(accountDTO.getUpPoint(),100));

                return upPoint;
            }
        }

        return null;
    }

    /**
     * 获取订单对应的店铺账期上浮值
     * @return 店铺设置的账期上浮值
     */
    private void calculateOrderMoney(B2bOrderConfirmRequest orderInfo) {
        // 如果是账期支付，需要上浮价格
        BigDecimal upPoint = this.getShopUpPoint(orderInfo);

        List<CreateOrderDetailRequest> createOrderDetailRequestList = orderInfo.getOrderDetailList();
        // 计算订单明细上的金额
        BigDecimal totalAmount = createOrderDetailRequestList.stream().peek(orderDetail -> {
            orderDetail.setGoodsPrice(orderDetail.getGoodsPrice());
            // 非以岭商品上浮价格
            if (upPoint != null && OrderGoodsTypeEnum.YLGOODS !=  OrderGoodsTypeEnum.getByCode(orderDetail.getGoodsType())){
                orderDetail.setGoodsPrice(NumberUtil.round(NumberUtil.mul(orderDetail.getGoodsPrice(),upPoint),2));
            }
            orderDetail.setGoodsAmount(NumberUtil.round(NumberUtil.mul(orderDetail.getGoodsPrice(), orderDetail.getGoodsQuantity()), 2));
        }).map(CreateOrderDetailRequest::getGoodsAmount).reduce(BigDecimal::add).get();

        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setFreightAmount(BigDecimal.ZERO);
        orderInfo.setPaymentAmount(orderInfo.getTotalAmount());
    }



    /**
     * 设置赠品信息集合信息
     * @param createOrderRequestList
     * @return 扣减赠品信息
     */
    private List<PromotionReduceStockDTO> setPromotionReduceStockList(List<B2bOrderConfirmRequest> createOrderRequestList) {

        return createOrderRequestList.stream().flatMap(t -> t.getOrderGiftRequestList().stream()).map(giftRequest -> {
                PromotionReduceStockDTO promotionReduceStockDTO = new PromotionReduceStockDTO();
                promotionReduceStockDTO.setPromotionActivityId(giftRequest.getPromotionActivityId());
                promotionReduceStockDTO.setGoodsGiftId(giftRequest.getGoodsGiftId());
            return promotionReduceStockDTO;

        }).collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(t -> StringUtils.join(t.getPromotionActivityId(),Constants.SEPARATOR_MIDDLELINE,t.getGoodsGiftId())))), ArrayList::new));
    }


    /**
     * 构建确认订单明细信息
     * @param opUserId 操作人
     * @param t 订单明细信息
     * @return
     */
    private CreateOrderDetailRequest buildOrderConfirmDetailRequest(Long opUserId,OrderDetailDTO t) {

        CreateOrderDetailRequest detailRequest = PojoUtils.map(t, CreateOrderDetailRequest.class);
        detailRequest.setOrderDetailId(t.getId());
        detailRequest.setGoodsPrice(t.getGoodsPrice());
        detailRequest.setGoodsQuantity(t.getGoodsQuantity());
        detailRequest.setGoodsId(t.getGoodsId());
        detailRequest.setGoodsSkuId(t.getGoodsSkuId());
        detailRequest.setGoodsAmount(t.getGoodsAmount());
        detailRequest.setOrderId(t.getOrderId());
        detailRequest.setDistributorGoodsId(t.getDistributorGoodsId());
        detailRequest.setGoodsType(t.getGoodsType());
        detailRequest.setOpUserId(opUserId);
        detailRequest.setOrderNo(t.getOrderNo());
        detailRequest.setOrderId(t.getOrderId());
        detailRequest.setPlatformPaymentDiscountAmount(BigDecimal.ZERO);
        detailRequest.setShopPaymentDiscountAmount(BigDecimal.ZERO);

        return detailRequest;
    }

    /**
     * 创建请求确认信息request
     * @param buyerEid 买家Eid
     * @param opUserId 创建人
     * @param distributorOrderDto 商家订单信息
     * @return
     */
    private B2bOrderConfirmRequest buildOrderConfirmRequest(Long buyerEid,Long opUserId,OrderConfirmRequest.DistributorOrderDTO distributorOrderDto) {


        B2bOrderConfirmRequest request = new B2bOrderConfirmRequest();
        request.setOrderSource(OrderSourceEnum.SA.getCode());
        request.setOrderType(OrderTypeEnum.B2B.getCode());
        request.setSplitOrderType(SplitOrderEnum.NORMAL.name());
        request.setOrderCategory(OrderCategoryEnum.NORMAL.getCode());
        request.setOrderId(distributorOrderDto.getOrderId());
        request.setOrderNo(distributorOrderDto.getOrderNo());
        request.setPaymentMethod(distributorOrderDto.getPaymentMethod());
        request.setOrderNote(distributorOrderDto.getBuyerMessage());
        request.setPaymentStatus(PaymentStatusEnum.UNPAID.getCode());
        request.setOrderStatus(OrderStatusEnum.UNAUDITED.getCode());
        request.setAuditStatus(OrderAuditStatusEnum.AUDIT_PASS.getCode());
        request.setCustomerConfirmStatus(CustomerConfirmEnum.CONFIRMED.getCode());
        request.setCustomerConfirmTime(new Date());
        request.setBuyerEid(buyerEid);
        request.setSellerEid(distributorOrderDto.getSellerEid());
        request.setDistributorEid(distributorOrderDto.getSellerEid());
        request.setPromotionActivityRequestList(Lists.newArrayList());
        request.setCouponDiscountAmount(BigDecimal.ZERO);
        request.setPlatformCouponDiscountAmount(BigDecimal.ZERO);
        request.setPlatformPaymentDiscountAmount(BigDecimal.ZERO);
        request.setShopPaymentDiscountAmount(BigDecimal.ZERO);

        List<OrderDetailDTO> orderDetailDTOList = orderDetailApi.getOrderDetailInfo(distributorOrderDto.getOrderId());

        if (CollectionUtil.isEmpty(orderDetailDTOList)) {

            throw new BusinessException(OrderErrorCode.ORDER_NOT_EXISTS);
        }

        List<CreateOrderDetailRequest> detailRequests = orderDetailDTOList.stream().map(t -> buildOrderConfirmDetailRequest(opUserId,t)).collect(Collectors.toList());

        request.setOrderDetailList(detailRequests);
        request.setOrderGiftRequestList(ListUtil.empty());
        request.setOrderCouponUseList(ListUtil.empty());

        return request;
    }


    /**
     * 发送用户确认订单消息
     * @param orderDTOList 订单信息
     * @return
     */
    private List<MqMessageBO> sendCustomerConfirmMessage(List<OrderDTO> orderDTOList) {

        // 无需在线支付的支付方式
        EnumSet<PaymentMethodEnum> hasPayMethods = EnumSet.of(PaymentMethodEnum.OFFLINE,PaymentMethodEnum.PREPAYMENT,PaymentMethodEnum.PAYMENT_DAYS);

        // 下单自动发券
        List<MqMessageBO> mqMessageBOList = orderDTOList.stream().map(orderDTO -> {

            MqMessageBO couponMessageBo = new MqMessageBO(Constants.TOPIC_ORDER_COUPON_AUTOMATIC_SEND, Constants.TAG_ORDER_COUPON_AUTOMATIC_CREATED, orderDTO.getOrderNo());
            couponMessageBo = mqMessageSendApi.prepare(couponMessageBo);
            return couponMessageBo;

        }).collect(Collectors.toList());

        // 无需在线支付订单自动审核
        List<MqMessageBO> mqMessageBOS = orderDTOList.stream().filter(orderDTO -> hasPayMethods.contains(PaymentMethodEnum.getByCode(orderDTO.getPaymentMethod().longValue())))
                .map(orderDTO -> {
                MqMessageBO orderAuditedBo = new MqMessageBO(Constants.TOPIC_ORDER_AUDITED, "", orderDTO.getOrderNo());
                // 如果订单支付方式为线下或者账期时订单状态变成“待发货”
                orderAuditedBo = mqMessageSendApi.prepare(orderAuditedBo);
                return orderAuditedBo;
        }).collect(Collectors.toList());

        // 自动发券以及自动审核
        if (CollectionUtil.isNotEmpty(mqMessageBOS)) {

            mqMessageBOList.addAll(mqMessageBOS);
        }

        return mqMessageBOList;

    }

    /**
     * 用户确认订单
     * @param orderConfirmRequest
     * @return
     */
    @GlobalTransactional
    public OrderSubmitBO customerConfirm(OrderConfirmRequest orderConfirmRequest) {

        // 订单号信息
        List<String> orderNoList = orderConfirmRequest.getDistributorOrderList().stream().map(t -> t.getOrderNo()).collect(Collectors.toList());
        // 平台优惠劵Id
        Long platformCustomerCouponId = CompareUtil.compare(0l, orderConfirmRequest.getPlatformCustomerCouponId(), true) >= 0 ? null : orderConfirmRequest.getPlatformCustomerCouponId();

        // 商家优惠劵ID
        Map<Long, Long> shopCustomerCouponIdMap = orderConfirmRequest.getDistributorOrderList()
                .stream()
                .filter(t -> t.getShopCustomerCouponId() != null && CompareUtil.compare(t.getShopCustomerCouponId(),0l) > 0 )
                .collect(Collectors.toMap(OrderConfirmRequest.DistributorOrderDTO::getSellerEid,OrderConfirmRequest.DistributorOrderDTO::getShopCustomerCouponId));

        // 确认订单请求信息
        List<B2bOrderConfirmRequest> b2bOrderConfirmRequestList = orderConfirmRequest.getDistributorOrderList().stream()
                .map(distributorOrderDto -> this.buildOrderConfirmRequest(orderConfirmRequest.getBuyerEid(),orderConfirmRequest.getOpUserId(),distributorOrderDto))
                .collect(Collectors.toList());

        // 1,重新计算订单金额
        b2bOrderConfirmRequestList.forEach(t -> calculateOrderMoney(t));

        // 2,计算营销优惠
        CalOrderDiscountContextBO<B2bOrderConfirmRequest> discountContextBO = new CalOrderDiscountContextBO();
        discountContextBO.setPlatformEnum(PlatformEnum.SALES_ASSIST);
        discountContextBO.setOrderTypeEnum(OrderTypeEnum.B2B);
        discountContextBO.setBuyerEid(orderConfirmRequest.getBuyerEid());
        discountContextBO.setOpUserId(orderConfirmRequest.getOpUserId());
        discountContextBO.setPlatformCustomerCouponId(platformCustomerCouponId);
        discountContextBO.setShopCustomerCouponIdMap(shopCustomerCouponIdMap);
        discountContextBO.setCreateOrderRequestList(b2bOrderConfirmRequestList);

        DiscountHandlerChain handlerChain = new DiscountHandlerChain(discountContextBO);
        handlerChain.addHandler(giftDiscountProcessHandler).addHandler(couponDiscountProcessHandler).processDiscount();

        // 3、校验账期余额是否充足
        this.checkPaymentAccount(b2bOrderConfirmRequestList);

        // 4,修改确认订单状态
        orderB2BApi.b2bOrderConfirm(b2bOrderConfirmRequestList);

        // 5,取消未确认订单
        Optional.ofNullable(orderConfirmRequest.getCancelOrderIds()).ifPresent(t -> t.forEach(z -> orderDeliveryAndReceiveProcessService.cancel(z,0l)));

        // 查询订单信息
        List<OrderDTO> orderDTOList = orderApi.listByOrderNos(orderNoList);

        // 6,如果存在在线支付订单，创建在线支付交易
        String payId = this.initPaymentOrder(orderDTOList);

        // 7,用户确认订单消息
        List<MqMessageBO> mqMessageBOList = this.sendCustomerConfirmMessage(orderDTOList);

        OrderSubmitBO submitBo = new OrderSubmitBO();
        submitBo.setPayId(payId);
        submitBo.setOrderDTOList(orderDTOList);
        submitBo.setMqMessageBOList(mqMessageBOList);

        /**
         * 扣减优惠信息
         */
        CreateOrderEvent createOrderEvent = new CreateOrderEvent(this,orderDTOList,true,false);
        createOrderEvent.setPlatformCustomerCouponId(orderConfirmRequest.getPlatformCustomerCouponId());
        createOrderEvent.setShopCustomerCouponIds(MapUtil.isEmpty(discountContextBO.getShopCustomerCouponIdMap()) ? null : ListUtil.toList(discountContextBO.getShopCustomerCouponIdMap().values()));
        createOrderEvent.setOrderSourceEnum(OrderSourceEnum.SA);
        createOrderEvent.setOrderTypeEnum(OrderTypeEnum.B2B);
        createOrderEvent.setUserId(orderConfirmRequest.getOpUserId());
        createOrderEvent.setPromotionReduceStockList(this.setPromotionReduceStockList(b2bOrderConfirmRequestList));
        createOrderEvent.setSaveOrderDevice(false);

        this.applicationEventPublisher.publishEvent(createOrderEvent);

        return submitBo;

    }

    @Override
    public OrderSubmitBO b2bConfirmCustomerOrder(OrderConfirmRequest orderConfirmRequest) {

        // 1:用户确认
        OrderSubmitBO submitBo = _this.customerConfirm(orderConfirmRequest);
        // 2:发送自动审核通过消息
        submitBo.getMqMessageBOList().forEach(mqMessageBO -> mqMessageSendApi.send(mqMessageBO));

        return submitBo;
    }


    /**
     * 校验账期余额是否充足
     * @param createOrderRequestList
     */
    private void checkPaymentAccount(List<B2bOrderConfirmRequest> createOrderRequestList) {

        List<B2bOrderConfirmRequest> paymentDayList = createOrderRequestList.stream().filter(e -> PaymentMethodEnum.PAYMENT_DAYS == PaymentMethodEnum.getByCode(e.getPaymentMethod().longValue())).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(paymentDayList)) {

            return;
        }

        /**将相同卖家的支付金额进行合并，防止出现拆单导致，相同的卖家出现多条的问题**/
        Long buyerEid = paymentDayList.stream().findFirst().get().getBuyerEid();
        Map<Long,BigDecimal> paymentAmountMap =  paymentDayList.stream().collect(Collectors.groupingBy(CreateOrderRequest::getSellerEid,Collectors.mapping(CreateOrderRequest::getPaymentAmount,Collectors.reducing(BigDecimal.ZERO,BigDecimal::add))));

        for (Map.Entry<Long,BigDecimal> entry : paymentAmountMap.entrySet()) {

            // 账期可用支付金额
            BigDecimal paymentDaysAvailableAmount = paymentDaysAccountApi.getB2bAvailableAmountByCustomerEid(entry.getKey(), buyerEid);

            // 订单应付金额
            if (paymentDaysAvailableAmount.compareTo(entry.getValue()) == -1) {

                throw new BusinessException(OrderErrorCode.ORDER_PAYMENT_DAY_ERROR);
            }
        }
    }

    @Override
    @GlobalTransactional
    public Boolean popConfirmOrder(PopOrderConfirmRequest popOrderConfirmRequest) {
        PaymentRequest request = PojoUtils.map(popOrderConfirmRequest, PaymentRequest.class);
        request.setOpUserId(popOrderConfirmRequest.getOpUserId());
        request.setOrderType(OrderTypeEnum.POP.getCode());
        List<PaymentRequest.OrderPaymentRequest> orderPaymentList = Lists.newArrayList();
        List<UpdateOrderRemarkRequest.OrderRemarkRequest> remarkRequests = Lists.newArrayList();
        for (PopOrderConfirmRequest.DistributorOrderDTO  distributorOrderDTO : popOrderConfirmRequest.getOrderPaymentList()) {
            PaymentRequest.OrderPaymentRequest orderPaymentRequest = new PaymentRequest.OrderPaymentRequest();
            orderPaymentRequest.setOrderId(distributorOrderDTO.getOrderId());
            orderPaymentRequest.setPaymentMethodId(distributorOrderDTO.getPaymentMethodId());
            orderPaymentList.add(orderPaymentRequest);
            if (StringUtils.isNotBlank(distributorOrderDTO.getBuyerMessage())) {
                UpdateOrderRemarkRequest.OrderRemarkRequest remarkRequest = new  UpdateOrderRemarkRequest.OrderRemarkRequest();
                remarkRequest.setOrderId(distributorOrderDTO.getOrderId());
                remarkRequest.setOrderRemark(distributorOrderDTO.getBuyerMessage());
                remarkRequests.add(remarkRequest);
            }
        }
        request.setOrderPaymentList(orderPaymentList);
        boolean result = paymentApi.pay(request);
        if (!result) {
            return false;
        }
        if (CollectionUtil.isEmpty(remarkRequests)) {
            return true;
        }
        Boolean updateResult = orderApi.updateOrderRemark(new UpdateOrderRemarkRequest().setRemarkRequests(remarkRequests));
        if (!updateResult) {
            return false;
        }
        return true;
    }


    /**
     * 用户操作取消
     * @param orderNoList
     * @return
     */
    @Override
    @GlobalTransactional
    public Result<Void> userCancelB2BOrder(List<String> orderNoList) {
        List<OrderDTO> listResult = orderApi.listByOrderNos(orderNoList);

        if (CollectionUtil.isEmpty(listResult)) {

            return Result.success();
        }

        listResult.forEach(e -> orderDeliveryAndReceiveProcessService.cancel(e.getId(),0l));

        return Result.success();
    }
}

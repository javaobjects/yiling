package com.yiling.mall.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.Constants;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.inventory.dto.request.AddOrSubtractQtyRequest;
import com.yiling.goods.medicine.api.GoodsStatisticsApi;
import com.yiling.goods.medicine.dto.GoodsRefreshDTO;
import com.yiling.goods.medicine.dto.request.StatisticsGoodsSaleRequest;
import com.yiling.mall.order.dto.request.OrderCouponUseReturnRequest;
import com.yiling.mall.order.dto.request.RefundOrderRequest;
import com.yiling.mall.order.service.OrderCouponUseService;
import com.yiling.mall.order.service.OrderDeliveryAndReceiveProcessService;
import com.yiling.mall.order.service.OrderRefundService;
import com.yiling.mall.payment.api.PaymentDaysOrderApi;
import com.yiling.marketing.integral.dto.request.AddIntegralRequest;
import com.yiling.marketing.integral.dto.request.QueryOrderIntegralRequest;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.request.PromotionUpdateBuyRecordRequest;
import com.yiling.marketing.promotion.dto.request.PromotionUpdateDetailRequest;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderB2BApi;
import com.yiling.order.order.api.OrderCouponUseApi;
import com.yiling.order.order.api.OrderDeliveryApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.PresaleOrderApi;
import com.yiling.order.order.dto.OrderCouponUseDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDeliveryDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.PresaleOrderDTO;
import com.yiling.order.order.dto.request.DeliveryInfoRequest;
import com.yiling.order.order.dto.request.QueryBackOrderInfoRequest;
import com.yiling.order.order.dto.request.SaveOrderDeliveryInfoRequest;
import com.yiling.order.order.dto.request.SaveOrderDeliveryListInfoRequest;
import com.yiling.order.order.dto.request.SaveOrderReceiveListInfoRequest;
import com.yiling.order.order.dto.request.UpdateOrderInvoiceInfoRequest;
import com.yiling.order.order.dto.request.UpdateOrderPaymentStatusRequest;
import com.yiling.order.order.enums.OrderCategoryEnum;
import com.yiling.order.order.enums.OrderCouponUseReturnTypeEnum;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderInvoiceApplyStatusEnum;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.order.order.enums.RefundSourceEnum;
import com.yiling.payment.enums.OrderPlatformEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.api.PayApi;
import com.yiling.payment.pay.dto.request.CloseOrderRequest;
import com.yiling.sales.assistant.message.api.MessageApi;
import com.yiling.sales.assistant.message.dto.request.OrderMessageBuildRequest;
import com.yiling.sales.assistant.message.dto.request.OrderMessageRequest;
import com.yiling.sales.assistant.message.enums.MessageNodeEnum;
import com.yiling.sales.assistant.message.enums.MessageRoleEnum;
import com.yiling.sales.assistant.message.enums.SourceEnum;
import com.yiling.user.integral.enums.IntegralRulePlatformEnum;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.request.RefundPaymentDaysAmountRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单删除,发货，收货接口
 *
 * @author:wei.wang
 * @date:2021/7/16
 */
@Service
@Slf4j
public class OrderDeliveryAndReceiveProcessServiceImpl implements OrderDeliveryAndReceiveProcessService {
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;
    @DubboReference
    OrderDetailApi orderDetailApi;
    @DubboReference
    InventoryApi inventoryApi;
    @DubboReference
    OrderB2BApi orderB2BApi;
    @DubboReference
    OrderCouponUseApi orderCouponUseApi;
    @DubboReference
    OrderDeliveryApi orderDeliveryApi;
    @DubboReference
    GoodsStatisticsApi goodsStatisticsApi;
    @DubboReference
    PayApi payApi;
    @DubboReference
    MessageApi messageApi;
    @DubboReference
    PromotionActivityApi promotionActivityApi;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;
    @DubboReference
    PresaleOrderApi presaleOrderApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    PaymentDaysOrderApi paymentDaysOrderApi;
    @Lazy
    @Autowired
    OrderDeliveryAndReceiveProcessServiceImpl _this;
    @Autowired
    OrderRefundService orderRefundService;
    @Autowired
    OrderCouponUseService orderCouponUseService;
    @Autowired
    RedisService redisService;

    @Override
    @GlobalTransactional
    public Boolean cancelOrderExpect(Long orderId, Long opUserId) {
        Boolean flag = orderApi.cancelOrderExpect(orderId, opUserId);
        if (flag) {
            //发送数据
            OrderDTO orderInfo = orderApi.getOrderInfo(orderId);
            paymentDaysHandle(opUserId,orderInfo);
            sendOrderMessage(orderInfo, orderInfo.getCreateUser(), MessageNodeEnum.CANCELLED);

        } else {
            throw new BusinessException(OrderErrorCode.ORDER_INFO_STATUS_ERROR);
        }
        return flag;
    }

    private void sendOrderMessage(OrderDTO orderInfo, Long opUserId, MessageNodeEnum messageNodeEnum) {

        if (OrderSourceEnum.getByCode(orderInfo.getOrderSource()) == OrderSourceEnum.SA) {
            OrderMessageBuildRequest orderMessageBuildRequest = new OrderMessageBuildRequest();
            OrderMessageRequest request = new OrderMessageRequest();
            request.setCode(orderInfo.getOrderNo());
            request.setOrderNo(orderInfo.getOrderNo());
            request.setCreateTime(orderInfo.getCreateTime());
            orderMessageBuildRequest.setOrderMessageRequest(request);
            orderMessageBuildRequest.setUserId(opUserId);
            orderMessageBuildRequest.setSourceEnum(SourceEnum.SA);
            orderMessageBuildRequest.setMessageRoleEnum(MessageRoleEnum.TO_USER);
            orderMessageBuildRequest.setMessageNodeEnum(messageNodeEnum);
            orderMessageBuildRequest.setOpUserId(opUserId);
            messageApi.buildOrderMessage(orderMessageBuildRequest);
        }
    }

    /**
     * 计算销售助手任务进度
     * @param orderInfo
     */
    private void sendSAOrderTaskRateMessage(OrderDTO orderInfo, List<MqMessageBO> mqMessageList) {

        if (OrderSourceEnum.getByCode(orderInfo.getOrderSource()) == OrderSourceEnum.SA) {

            MqMessageBO mqMessageTwoBO = new MqMessageBO(Constants.TOPIC_SA_ORDER_TASK_RATE_SEND, Constants.TAG_SA_ORDER_TASK_RATE_SEND, orderInfo.getOrderNo());
            mqMessageTwoBO = mqMessageSendApi.prepare(mqMessageTwoBO);
            mqMessageList.add(mqMessageTwoBO);


        }

    }


    private void cancelOrderReturnNumber(Long orderId, Long opUserId) {
        List<OrderDetailDTO> detailLists = orderDetailApi.getOrderDetailInfo(orderId);
        if (CollectionUtil.isNotEmpty(detailLists)) {
            //List<Long> gids = new ArrayList<>();
            for (OrderDetailDTO one : detailLists) {
                AddOrSubtractQtyRequest request = new AddOrSubtractQtyRequest();
                request.setFrozenQty(Long.valueOf(one.getGoodsQuantity()));
                request.setSkuId(one.getGoodsSkuId());
                request.setOpUserId(opUserId);
                request.setOpTime(new Date());
                request.setOrderNo(one.getOrderNo());
                log.info("订单退回库存数量,AddOrSubtractQtyRequest:{}", JSON.toJSONString(request));
                inventoryApi.subtractFrozenQty(request);

            }
        }
    }

    private void deliveryOrderReturnNumber(Long orderId, Long opUserId, Map<Long, Integer> mapNumber) {
        List<OrderDetailDTO> detailLists = orderDetailApi.getOrderDetailInfo(orderId);
        if (CollectionUtil.isNotEmpty(detailLists)) {
            //List<Long> gids = new ArrayList<>();
            Map<Long, OrderDetailDTO> detailMap = detailLists.stream().collect(Collectors.toMap(OrderDetailDTO::getId, o -> o, (k1, k2) -> k1));
            for (Map.Entry<Long, Integer> entry : mapNumber.entrySet()) {
                OrderDetailDTO detailDTO = detailMap.get(entry.getKey());
                AddOrSubtractQtyRequest request = new AddOrSubtractQtyRequest();
                request.setFrozenQty(Long.valueOf(entry.getValue()));
                request.setSkuId(detailDTO.getGoodsSkuId());
                request.setOpUserId(opUserId);
                request.setOpTime(new Date());
                request.setOrderNo(detailDTO.getOrderNo());
                request.setQty(Long.valueOf(entry.getValue()));
                log.info("订单发货退回库存数量,AddOrSubtractQtyRequest:{}", JSON.toJSONString(request));
                inventoryApi.subtractFrozenQtyAndQty(request);

            }

        }
    }

    /**
     * 发货
     *
     * @param request
     * @return
     */
    @GlobalTransactional
    @Override
    public Boolean delivery(SaveOrderDeliveryListInfoRequest request) {

        //判断是否需要取消订单
        Integer allNumber = 0;
        Map<Long, Integer> mapNumber = new HashMap<>();
        for (SaveOrderDeliveryInfoRequest detailOne : request.getOrderDeliveryGoodsInfoList()) {
            Integer number = 0;
            for (DeliveryInfoRequest deliveryOne : detailOne.getDeliveryInfoList()) {
                number = number + deliveryOne.getDeliveryQuantity();
            }
            mapNumber.put(detailOne.getDetailId(), number);
            allNumber = allNumber + number;
        }
        List<MqMessageBO> mqMessageList = new ArrayList<>();
        if (allNumber.compareTo(0) > 0) {
            //发货
            orderApi.delivery(request);

            //发送消息
            OrderDTO orderInfo = orderApi.getOrderInfo(request.getOrderId());

            //扣减库存
            if( OrderCategoryEnum.NORMAL == OrderCategoryEnum.getByCode(orderInfo.getOrderCategory())){
                deliveryOrderReturnNumber(request.getOrderId(), request.getOpUserId(), mapNumber);
            }

            if (OrderStatusEnum.getByCode(orderInfo.getOrderStatus()) == OrderStatusEnum.DELIVERED) {
                sendOrderMessage(orderInfo, orderInfo.getCreateUser(), MessageNodeEnum.SHIPPED);
                mqMessageList  = sendCompleteDeliveryOrder(request.getOrderId());
            } else {
                sendOrderMessage(orderInfo, orderInfo.getCreateUser(), MessageNodeEnum.PARTIAL_SHIPMENT);
            }

            if(OrderInvoiceApplyStatusEnum.INVOICED == OrderInvoiceApplyStatusEnum.getByCode(orderInfo.getInvoiceStatus())){
                UpdateOrderInvoiceInfoRequest saveOrder = new UpdateOrderInvoiceInfoRequest();
                saveOrder.setId(orderInfo.getId());
                saveOrder.setInvoiceStatus(OrderInvoiceApplyStatusEnum.PENDING_APPLY.getCode());
                orderApi.updateOrderInvoiceInfo(saveOrder);
            }

            if (CollUtil.isNotEmpty(mqMessageList)) {
                mqMessageList.forEach(mqMessageBO -> mqMessageSendApi.send(mqMessageBO));
            }
        }

        return true;
    }

    /**
     * 关闭发货
     *
     * @param orderId 订单id
     * @return
     */
    @Override
    public Boolean closeDelivery(Long opUserId,Long orderId) {
        List<MqMessageBO> mqMessageList = _this.saveCloseDeliveryInfo(opUserId, orderId);

        if (CollUtil.isNotEmpty(mqMessageList)) {
            mqMessageList.forEach(mqMessageBO -> mqMessageSendApi.send(mqMessageBO));
        }
        return true;
    }

    @GlobalTransactional
    public List<MqMessageBO>  saveCloseDeliveryInfo(Long opUserId,Long orderId){
        OrderDTO orderInfo = orderApi.getOrderInfo(orderId);
        List<OrderDeliveryDTO> orderDeliveryList = orderDeliveryApi.getOrderDeliveryList(orderId);
        List<MqMessageBO> mqMessageList = ListUtil.toList();
        if (CollectionUtil.isNotEmpty(orderDeliveryList)) {
            if (OrderStatusEnum.getByCode(orderInfo.getOrderStatus()) != OrderStatusEnum.DELIVERED) {
                orderApi.closeDelivery(orderId, opUserId);
                mqMessageList = sendCompleteDeliveryOrder(orderId);
                sendOrderMessage(orderInfo, orderInfo.getCreateUser(), MessageNodeEnum.SHIPPED);
            }

        } else {
            Boolean result = orderApi.cancel(orderId, 0L, true);
            if (result) {
                paymentDaysHandle(opUserId, orderInfo);
                sendOrderMessage(orderInfo, orderInfo.getCreateUser(), MessageNodeEnum.CANCELLED);
                //mqMessageList = sendCompleteDeliveryOrder(orderId);
            }
        }

        return mqMessageList;
    }


    /**
     * 页面发货接口
     *
     * @param request
     * @return
     */
    @Override
    public Boolean frontDelivery(SaveOrderDeliveryListInfoRequest request) {

        List<MqMessageBO> mqMessageList = _this.frontDeliverySendMessage(request);
        if (CollUtil.isNotEmpty(mqMessageList)) {
            mqMessageList.forEach(mqMessageBO -> mqMessageSendApi.send(mqMessageBO));
        }
        return true;
    }

    @GlobalTransactional
    public List<MqMessageBO> frontDeliverySendMessage (SaveOrderDeliveryListInfoRequest request){
        delivery(request);
        List<MqMessageBO> mqMessageList = _this.saveCloseDeliveryInfo(request.getOpUserId(), request.getOrderId());

        //        OrderDTO orderInfo = orderApi.getOrderInfo(request.getOrderId());
        //        List<OrderDetailDTO> orderDetailList = orderDetailApi.getOrderDetailInfo(orderInfo.getId());
        //        List<String> inSnList = orderDetailList.stream().filter(s -> StringUtils.isNotBlank(s.getGoodsErpCode())).map(o -> o.getGoodsErpCode()).collect(Collectors.toList());
        //        if (CollectionUtil.isNotEmpty(inSnList)) {
        //            GoodsRefreshDTO goodsRefreshDTO = new GoodsRefreshDTO();
        //            goodsRefreshDTO.setEid(orderInfo.getSellerEid());
        //            goodsRefreshDTO.setInSnList(inSnList);
        //            //发送刷新库存mq消息
        //            log.info("刷新库存mq信息topic_goods_qty_refresh参数：{}", JSON.toJSONString(goodsRefreshDTO));
        //            MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_GOODS_QTY_REFRESH_SEND, Constants.TAG_GOODS_QTY_REFRESH_SEND, JSON.toJSONString(goodsRefreshDTO));
        //            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        //            mqMessageList.add(mqMessageBO);
        //        }
        return mqMessageList;
    }


    private  List<MqMessageBO> sendCompleteDeliveryOrder(Long orderId) {
        List<MqMessageBO> mqMessageBOList = ListUtil.toList();
        //提示已经发货成功
        OrderDTO orderInfo = orderApi.getOrderInfo(orderId);

        if (OrderStatusEnum.getByCode(orderInfo.getOrderStatus()) == OrderStatusEnum.DELIVERED) {

            if( OrderTypeEnum.getByCode(orderInfo.getOrderType()) == OrderTypeEnum.B2B){

                MqMessageBO mqMessageTwoBO = new MqMessageBO(Constants.TOPIC_ORDER_COUPON_AUTOMATIC_SEND, Constants.TAG_ORDER_COUPON_AUTOMATIC_DELIVERED, orderInfo.getOrderNo());
                mqMessageTwoBO = mqMessageSendApi.prepare(mqMessageTwoBO);
                mqMessageBOList.add(mqMessageTwoBO);
            }
            log.info("订单发货MQ发送成功,订单单号,orderNo:{}", orderInfo.getOrderNo());
            MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_ORDER_RETURN, Constants.TAG_ORDER_DELIVERED, orderInfo.getOrderNo(), orderInfo.getId().intValue());
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
            mqMessageBOList.add(mqMessageBO);

            MqMessageBO orderErpPushBo = new MqMessageBO(Constants.TOPIC_ORDER_PUSH_ERP, Constants.TAG_ORDER_PUSH_ERP, JSON.toJSONString(Collections.singleton(orderInfo.getId())));
            orderErpPushBo = mqMessageSendApi.prepare(orderErpPushBo);
            mqMessageBOList.add(orderErpPushBo);
        }
        return mqMessageBOList;
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @param opUserId
     */
    @Override
    @GlobalTransactional
    public Boolean cancel(Long orderId, Long opUserId) {
        OrderDTO orderInfo = orderApi.getOrderInfo(orderId);
        Boolean result = false;
        if (OrderTypeEnum.getByCode(orderInfo.getOrderType()) == OrderTypeEnum.POP) {
            result = orderApi.cancel(orderId, opUserId, true);
        } else if (OrderTypeEnum.getByCode(orderInfo.getOrderType()) == OrderTypeEnum.B2B) {
            // 如果为在线支付，需要查询下在线支付交易状态
            if (PaymentMethodEnum.ONLINE == PaymentMethodEnum.getByCode(orderInfo.getPaymentMethod().longValue())
                    && (PaymentStatusEnum.UNPAID == PaymentStatusEnum.getByCode(orderInfo.getPaymentStatus())
                    || PaymentStatusEnum.PARTPAID == PaymentStatusEnum.getByCode(orderInfo.getPaymentStatus()))) {
                result = this.cancelOnlineOrder(orderInfo,opUserId);
            } else {
                result = orderB2BApi.B2BCancel(orderId, opUserId);
            }
        }

        if (result) {
            paymentDaysHandle(opUserId, orderInfo);
            cancelPromotionActivityReturnQuantity(orderId,opUserId);
            sendOrderMessage(orderInfo, orderInfo.getCreateUser(), MessageNodeEnum.CANCELLED);

        }
        return result;
    }


    /**
     * 取消在线支付未支付订单，需要检查下在线支付订单是否支付成功
     * @param orderInfo 订单信息
     * @param opUserId  操作人
     * @return 成功/失败
     */
    private boolean cancelOnlineOrder(OrderDTO orderInfo,Long opUserId) {
        boolean result = false;
        Result<Boolean> payResult;
        // 如果是预售订单
        if (OrderCategoryEnum.PRESALE == OrderCategoryEnum.getByCode(orderInfo.getOrderCategory())) {
            // 订单为部分支付，查询尾款
            if (PaymentStatusEnum.PARTPAID == PaymentStatusEnum.getByCode(orderInfo.getPaymentStatus())) {
                payResult =  payApi.orderQueryByOrderNo(OrderPlatformEnum.B2B,TradeTypeEnum.BALANCE,orderInfo.getOrderNo());
            } else {
                payResult =  payApi.orderQueryByOrderNo(OrderPlatformEnum.B2B,TradeTypeEnum.DEPOSIT,orderInfo.getOrderNo());
            }
        } else {
            payResult =  payApi.orderQueryByOrderNo(OrderPlatformEnum.B2B,TradeTypeEnum.PAY,orderInfo.getOrderNo());
        }

        if (!payResult.getData()) {
            result = orderB2BApi.B2BCancel(orderInfo.getId(), opUserId);
        } else {
            log.info("订单号：{}已支付成功，取消失败!",orderInfo.getOrderNo());
        }

        if (result) {
            this.sendClosePayOrderMessage(OrderPlatformEnum.B2B,orderInfo);
        }

        return result;
    }


    /**
     * 发送订单关闭消息
     * @param orderPlatformEnum
     * @param orderInfo
     */
    private void sendClosePayOrderMessage(OrderPlatformEnum orderPlatformEnum,OrderDTO orderInfo) {
        CloseOrderRequest request = new CloseOrderRequest();
        request.setAppOrderNo(orderInfo.getOrderNo());
        request.setOrderPlatform(orderPlatformEnum.getCode());
        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_PAY_TRADE_CLOSE_NOTIFY, Constants.TOPIC_PAY_TRADE_CLOSE_NOTIFY, JSON.toJSONString(request));
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        mqMessageSendApi.send(mqMessageBO);
    }

    /**
     *  取消订单秒杀特价归还数量
     * @param orderId
     * @param opUserId
     */
    private void cancelPromotionActivityReturnQuantity(Long orderId,Long opUserId){
        List<OrderDetailDTO> orderDetailDTO = orderDetailApi.getOrderDetailInfo(orderId);
        List<PromotionUpdateDetailRequest> returnPromotionList = new ArrayList<>();
        for (OrderDetailDTO detailOne : orderDetailDTO) {
            if (PromotionActivityTypeEnum.getByCode(detailOne.getPromotionActivityType()) != PromotionActivityTypeEnum.NORMAL) {
                PromotionUpdateDetailRequest promotionUpdateDetailRequest = new PromotionUpdateDetailRequest();
                promotionUpdateDetailRequest.setGoodsId(detailOne.getDistributorGoodsId());
                promotionUpdateDetailRequest.setQuantity(detailOne.getGoodsQuantity());
                returnPromotionList.add(promotionUpdateDetailRequest);
            }
        }
        if (CollectionUtil.isNotEmpty(returnPromotionList)) {
            PromotionUpdateBuyRecordRequest recordRequest = new PromotionUpdateBuyRecordRequest();
            recordRequest.setOrderId(orderId);
            recordRequest.setDetailList(returnPromotionList);
            recordRequest.setOpUserId(opUserId);
            promotionActivityApi.updateBuyRecordQuantity(recordRequest);
        }
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @param opUserId
     * @return
     */
    @Override
    @GlobalTransactional
    public Boolean cancelOrderB2BAdmin(Long orderId, Long opUserId) {
        OrderDTO orderInfo = orderApi.getOrderInfo(orderId);
        //取消订单
        Boolean result = orderApi.cancel(orderId, opUserId, true);
        if (result) {
            paymentDaysHandle(opUserId, orderInfo);
            cancelPromotionActivityReturnQuantity(orderId,opUserId);

            sendOrderMessage(orderInfo, orderInfo.getCreateUser(), MessageNodeEnum.CANCELLED);

        }
        return result;
    }

    /**
     * 收货
     *
     * @param orderId
     * @param opUserId
     * @return
     */
    @Override
    @GlobalTransactional
    public Boolean B2BReceive(Long orderId, Long opUserId, Long buyerEid) {
        Boolean result = orderB2BApi.B2BReceive(orderId, opUserId);
        if (result) {
            List<MqMessageBO> mqMessageList = ListUtil.toList();
            OrderDTO order = orderB2BApi.getB2BOrderOne(orderId);
            sendOrderMessage(order, order.getCreateUser(), MessageNodeEnum.RECEIVED);
            sendSAOrderTaskRateMessage(order,mqMessageList);
            if (OrderTypeEnum.getByCode(order.getOrderType()) == OrderTypeEnum.B2B) {

                MqMessageBO mqMessageTwoBO = new MqMessageBO(Constants.TOPIC_B2B_ORDER_RECEIVE_STATE_SEND, Constants.TAG_B2B_ORDER_RECEIVE_STATE_SEND, order.getOrderNo());
                mqMessageTwoBO = mqMessageSendApi.prepare(mqMessageTwoBO);
                mqMessageList.add(mqMessageTwoBO);

            }
            //收货发送erp订单
            MqMessageBO orderErpPushBo = new MqMessageBO(Constants.TOPIC_ORDER_PUSH_ERP, Constants.TAG_ORDER_PUSH_ERP, JSON.toJSONString(Collections.singleton(orderId)));
            orderErpPushBo = mqMessageSendApi.prepare(orderErpPushBo);
            mqMessageList.add(orderErpPushBo);


            QueryOrderIntegralRequest integralRecord = new QueryOrderIntegralRequest();
            integralRecord.setOrderNo(order.getOrderNo());
            integralRecord.setOpUserId(opUserId);
            MqMessageBO orderIntegralBo = new MqMessageBO(Constants.TOPIC_B2B_ORDER_GIVE_INTEGRAL_SEND, "", JSON.toJSONString(integralRecord));
            orderIntegralBo = mqMessageSendApi.prepare(orderIntegralBo);
            mqMessageList.add(orderIntegralBo);


            if (CollUtil.isNotEmpty(mqMessageList)) {
                mqMessageList.forEach(mqMessageBO -> mqMessageSendApi.send(mqMessageBO));
            }
        }
        return result;
    }

    /**
     * 收货发送积分数据
     * @param orderNo 订单
     * @return
     */
    @Override
    public AddIntegralRequest getIntegralRecord(String orderNo,Long opUserId){
        OrderDTO order = orderApi.selectByOrderNo(orderNo);
        List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderId(order.getId());
        List<OrderDetailDTO> orderDetailInfo = orderDetailApi.getOrderDetailInfo(order.getId());
        Map<Long, OrderDetailDTO> map = orderDetailInfo.stream().collect(Collectors.toMap(o -> o.getId(), o -> o));

        AddIntegralRequest result = new AddIntegralRequest();

        List<AddIntegralRequest.GoodsInfo> goodsInfoList = new ArrayList<>();
        BigDecimal amount = BigDecimal.ZERO;
        for(OrderDetailChangeDTO changeOne : orderDetailChangeList){
            AddIntegralRequest.GoodsInfo goods = new AddIntegralRequest.GoodsInfo();
            OrderDetailDTO detailDTO = map.get(changeOne.getDetailId());
            goods.setGoodsId(detailDTO.getDistributorGoodsId());
            goods.setGoodsNum(changeOne.getReceiveQuantity());
            goods.setGoodsAmount(changeOne.getReceiveAmount()
                    .subtract(changeOne.getReceiveCouponDiscountAmount())
                    .subtract(changeOne.getReceiveCashDiscountAmount())
                    .subtract(changeOne.getReceivePlatformCouponDiscountAmount())
                    .subtract(changeOne.getReturnPresaleDiscountAmount())
                    .subtract(changeOne.getReceiveTicketDiscountAmount()));
            goodsInfoList.add(goods);
            amount = amount.add(changeOne.getReceiveAmount());
        }
        result.setGoodsInfoList(goodsInfoList);

        result.setPlatform(IntegralRulePlatformEnum.B2B.getCode());
        result.setUid(order.getBuyerEid());
        result.setOrderId(order.getId());
        result.setOrderNo(order.getOrderNo());
        result.setEid(order.getSellerEid());
        result.setOrderAmount(amount);
        result.setPaymentMethod(order.getPaymentMethod());
        result.setOpUserId(opUserId);
        return result;
    }

    @Override
    @GlobalTransactional
    public Boolean confirmPaymentDayRepayment(Long orderId, Long opUserId) {
        boolean isSuccess = paymentDaysOrderApi.updatePaymentOrderAmount(orderId);
        if (!isSuccess) {
            return false;
        }
        UpdateOrderPaymentStatusRequest request = new UpdateOrderPaymentStatusRequest();
        request.setOrderId(orderId);
        request.setOpUserId(opUserId);
        request.setPaymentTime(new Date());
        request.setPaymentStatus(PaymentStatusEnum.PAYMENT_DAY_RETURN.getCode());
        return orderApi.updatePaymentStatus(request);
    }


    /**
     * 自动收货
     *
     * @param day
     * @return
     */
    @Override
    public Boolean activeB2BReceive(Integer day) {
        orderB2BApi.activeB2BReceive(day);
        return true;
    }

    /**
     * 自动收货根据订单ID
     * @param orderId
     * @return
     */
    @GlobalTransactional
    @Override
    public Boolean activeB2BReceiveByOrderId(Long orderId){
        log.info("B2B订单自动收货订单id:{}", JSON.toJSONString(orderId));
        Boolean result = orderB2BApi.activeB2BReceiveByOrderId(orderId);
        if(result){
            List<MqMessageBO> mqMessageList =  ListUtil.toList();
            OrderDTO one = orderApi.getOrderInfo(orderId);
            sendOrderMessage(one, one.getCreateUser(), MessageNodeEnum.RECEIVED);
            sendSAOrderTaskRateMessage(one,mqMessageList);
            if (OrderTypeEnum.getByCode(one.getOrderType()) == OrderTypeEnum.B2B) {
                MqMessageBO mqMessageTwoBO = new MqMessageBO(Constants.TOPIC_B2B_ORDER_RECEIVE_STATE_SEND, Constants.TAG_B2B_ORDER_RECEIVE_STATE_SEND, one.getOrderNo());
                mqMessageTwoBO = mqMessageSendApi.prepare(mqMessageTwoBO);
                mqMessageList.add(mqMessageTwoBO);

                QueryOrderIntegralRequest integralRecord = new QueryOrderIntegralRequest();
                integralRecord.setOrderNo(one.getOrderNo());
                integralRecord.setOpUserId(0l);
                MqMessageBO orderIntegralBo = new MqMessageBO(Constants.TOPIC_B2B_ORDER_GIVE_INTEGRAL_SEND, "", JSON.toJSONString(integralRecord));
                orderIntegralBo = mqMessageSendApi.prepare(orderIntegralBo);
                mqMessageList.add(orderIntegralBo);
            }
            //收货发送erp订单
            MqMessageBO orderErpPushBo = new MqMessageBO(Constants.TOPIC_ORDER_PUSH_ERP, Constants.TAG_ORDER_PUSH_ERP, JSON.toJSONString(Collections.singleton(orderId)));
            orderErpPushBo = mqMessageSendApi.prepare(orderErpPushBo);
            mqMessageList.add(orderErpPushBo);

            if (CollUtil.isNotEmpty(mqMessageList)) {
                mqMessageList.forEach(mqMessageBO -> mqMessageSendApi.send(mqMessageBO));
            }
        }


        return result;
    }


    /**
     * 获取订单信息(运营后台),要根据
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderDTO> getBackOrderPage(QueryBackOrderInfoRequest request) {

        return orderApi.getBackOrderPage(request);
    }

    /**
     * 统计前天收货数量
     *
     * @param orderTypeList 订单类型
     * @return
     */
    @Override
    public Boolean countReceiveOrder(List<Integer> orderTypeList) {
        String date = (String) redisService.get(RedisKey.generate("order", "receive", "count", "time"));
        List<Long> orderList;
        log.info("订单统计B2B数据统计redis保存时间date:{}", date);
        if (StringUtils.isEmpty(date)) {

            orderList = orderApi.countReceiveOrder(orderTypeList, null);
        } else {
            orderList = orderApi.countReceiveOrder(orderTypeList, date);
        }
        if(CollectionUtil.isNotEmpty(orderList)){
            for(Long id : orderList){
                List<OrderDetailDTO> orderDetailInfo = orderDetailApi.getOrderDetailInfo(id);
                for (OrderDetailDTO entry :orderDetailInfo) {
                    StatisticsGoodsSaleRequest request = new StatisticsGoodsSaleRequest();
                    request.setGoodsId(entry.getDistributorGoodsId());
                    request.setB2bSaleNumber(Long.valueOf(entry.getDeliveryQuantity()));
                    request.setOpUserId(0L);
                    request.setOpTime(DateUtil.date());
                    log.info("订单统计B2B数据参数为statisticsGoodsSaleRequest:{}", JSON.toJSONString(request));
                    goodsStatisticsApi.statisticsGoodsSale(request);
                }

            }
        }

        redisService.set(RedisKey.generate("order", "receive", "count", "time"), DateUtil.now());

        return true;
    }

    /**
     * 收货
     *
     * @param request
     * @return
     */
    @Override
    public Boolean receive(SaveOrderReceiveListInfoRequest request) {
        Boolean flag = orderApi.receive(request);
        if (flag) {
            OrderDTO orderInfo = orderApi.getOrderInfo(request.getOrderId());
            sendOrderMessage(orderInfo, orderInfo.getCreateUser(), MessageNodeEnum.RECEIVED);
            List<MqMessageBO> mqMessageList =  ListUtil.toList();

            sendSAOrderTaskRateMessage(orderInfo,mqMessageList);
            //收货发送erp订单
            MqMessageBO orderErpPushBo = new MqMessageBO(Constants.TOPIC_ORDER_PUSH_ERP, Constants.TAG_ORDER_PUSH_ERP, JSON.toJSONString(Collections.singleton(orderInfo.getId())));
            orderErpPushBo = mqMessageSendApi.prepare(orderErpPushBo);
            mqMessageList.add(orderErpPushBo);

            if (CollUtil.isNotEmpty(mqMessageList)) {
                mqMessageList.forEach(mqMessageBO -> mqMessageSendApi.send(mqMessageBO));
            }
        }

        return flag;
    }

    @Override
    public Boolean secondBusinessAutomaticReceive(List<Long> eidList) {
        List<Long> longs = orderApi.secondBusinessAutomaticReceive(eidList);
        if (CollectionUtil.isNotEmpty(longs)) {
            List<OrderDTO> orderList = orderApi.listByIds(longs);

            List<MqMessageBO> mqMessageList =  ListUtil.toList();
            for (OrderDTO one : orderList) {
                sendOrderMessage(one, one.getCreateUser(), MessageNodeEnum.RECEIVED);
                sendSAOrderTaskRateMessage(one,mqMessageList);
                //收货发送erp订单
                MqMessageBO orderErpPushBo = new MqMessageBO(Constants.TOPIC_ORDER_PUSH_ERP, Constants.TAG_ORDER_PUSH_ERP, JSON.toJSONString(Collections.singleton(one.getId())));
                orderErpPushBo = mqMessageSendApi.prepare(orderErpPushBo);
                mqMessageList.add(orderErpPushBo);
            }
            if (CollUtil.isNotEmpty(mqMessageList)) {
                mqMessageList.forEach(mqMessageBO -> mqMessageSendApi.send(mqMessageBO));
            }
        }
        return true;
    }

    private void paymentDaysHandle(Long opUserId, OrderDTO orderInfo) {
        Boolean presaleFlag = true;
        PresaleOrderDTO presaleOrder = presaleOrderApi.getOrderInfo(orderInfo.getId());
        //预售不暂用库存
        if( OrderCategoryEnum.NORMAL == OrderCategoryEnum.getByCode(orderInfo.getOrderCategory())){
            //解锁数量
            cancelOrderReturnNumber(orderInfo.getId(), opUserId);

        }else if (OrderCategoryEnum.PRESALE == OrderCategoryEnum.getByCode(orderInfo.getOrderCategory())){
            //预售活动取消发送消息
            if(presaleOrder != null){
                if(PaymentStatusEnum.PARTPAID == PaymentStatusEnum.getByCode(orderInfo.getPaymentStatus()) &&
                        System.currentTimeMillis() > presaleOrder.getBalanceEndTime().getTime() ){
                    presaleFlag = false;
                    log.info("预售活动取消发送消息,订单编号OrderNo:{}", JSON.toJSONString(orderInfo.getOrderNo()));
                    MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_B2B_PRESALE_ORDER_CANCEL_SEND, Constants.TAG_B2B_PRESALE_ORDER_CANCEL_SEND, orderInfo.getOrderNo());
                    mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
                    mqMessageSendApi.send(mqMessageBO);
                }
            }


        }

        //去掉账期
        if (PaymentMethodEnum.PAYMENT_DAYS.getCode().equals(Long.valueOf(orderInfo.getPaymentMethod())) && PaymentStatusEnum.getByCode(orderInfo.getPaymentStatus()) == PaymentStatusEnum.UNPAID) {
            RefundPaymentDaysAmountRequest paymentDaysAmountRequest = new RefundPaymentDaysAmountRequest();
            paymentDaysAmountRequest.setOrderId(orderInfo.getId())
                    .setRefundAmount(orderInfo.getPaymentAmount());
            paymentDaysAmountRequest.setPlatformEnum(PlatformEnum.getByCode(orderInfo.getOrderType()));
            paymentDaysAmountRequest.setOpTime(new Date());
            paymentDaysAmountRequest.setOpUserId(opUserId);
            log.info("订单取消,退还账期参数,paymentDaysAmountRequest:{}", JSON.toJSONString(paymentDaysAmountRequest));
            paymentDaysAccountApi.refund(paymentDaysAmountRequest);
        } else if (PaymentMethodEnum.ONLINE.getCode().equals(Long.valueOf(orderInfo.getPaymentMethod()))
                && presaleFlag ) {
            RefundOrderRequest request = new RefundOrderRequest();
            request.setBuyerEid(orderInfo.getBuyerEid());
            request.setSellerEid(orderInfo.getSellerEid());
            request.setTotalAmount(orderInfo.getTotalAmount());
            request.setPaymentAmount(orderInfo.getPaymentAmount());
            request.setOrderId(orderInfo.getId());
            request.setOrderNo(orderInfo.getOrderNo());
            request.setRefundType(1);
            request.setReason("取消订单");
            request.setOpUserId(opUserId);
            request.setOpTime(new Date());
            request.setRefundSource(RefundSourceEnum.NORMAL.getCode());

            if (PaymentStatusEnum.PAID == PaymentStatusEnum.getByCode(orderInfo.getPaymentStatus())){

                request.setRefundAmount(orderInfo.getPaymentAmount());
                log.info("订单线下支付取消订单退款参数RefundOrderRequest：{}", JSON.toJSONString(request));
                orderRefundService.refundOrder(request);
            } else if (PaymentStatusEnum.PARTPAID == PaymentStatusEnum.getByCode(orderInfo.getPaymentStatus())){

                request.setRefundAmount(presaleOrder.getDepositAmount());
                log.info("订单线下支付取消订单定金退款参数RefundOrderRequest：{}", JSON.toJSONString(request));
                orderRefundService.refundOrder(request);
            }
        }
        if (OrderTypeEnum.getByCode(orderInfo.getOrderType()) == OrderTypeEnum.B2B) {
            List<Long> couponIdList = new ArrayList<>();
            // 退还商家卷
            List<OrderCouponUseDTO> orderCouponUse = orderCouponUseApi.listOrderCouponReturnType(orderInfo.getId(), 2, OrderCouponUseReturnTypeEnum.NOT_RETURN.getCode());
            if (CollectionUtil.isNotEmpty(orderCouponUse)) {
                List<Long> list = orderCouponUse.stream().map(order -> order.getCouponId()).collect(Collectors.toList());
                couponIdList.addAll(list);
            }
            //退还平台卷
            List<OrderCouponUseDTO> orderCouponUseList = orderCouponUseApi.listOrderCouponReturnType(orderInfo.getId(), 1, OrderCouponUseReturnTypeEnum.NOT_RETURN.getCode());
            if (CollectionUtil.isNotEmpty(orderCouponUseList)) {
                List<Long> ids = orderCouponUseList.stream().map(order -> order.getCouponId()).collect(Collectors.toList());
                List<OrderCouponUseDTO> orderCouponUseDTOS = orderCouponUseApi.listByCouponIdList(ids);
                List<Long> orderIds = orderCouponUseDTOS.stream().filter(t -> OrderCouponUseReturnTypeEnum.NOT_RETURN == OrderCouponUseReturnTypeEnum.getByCode(t.getCouponType())).map(order -> order.getOrderId()).collect(Collectors.toList());
                List<OrderDTO> orderList = orderApi.listByIds(orderIds);
                Map<Long, OrderDTO> orderMap = orderList.stream().collect(Collectors.toMap(OrderDTO::getId, Function.identity()));
                Map<Long, List<OrderCouponUseDTO>> map = orderCouponUseDTOS.stream().collect(Collectors.groupingBy(OrderCouponUseDTO::getCouponId));
                for (Map.Entry<Long, List<OrderCouponUseDTO>> entry : map.entrySet()) {
                    List<OrderCouponUseDTO> valueList = entry.getValue();
                    Boolean flag = true;
                    for (OrderCouponUseDTO one : valueList) {
                        OrderDTO orderDTO = orderMap.get(one.getOrderId());
                        if (OrderStatusEnum.CANCELED != OrderStatusEnum.getByCode(orderDTO.getOrderStatus())) {
                            flag = false;
                        }
                    }
                    if (flag) {
                        couponIdList.add(entry.getKey());
                    }
                }

            }
            if (CollectionUtil.isNotEmpty(couponIdList)) {
                OrderCouponUseReturnRequest couponRequest = new OrderCouponUseReturnRequest();
                couponRequest.setOpUserId(opUserId);
                couponRequest.setCouponIdList(couponIdList);
                log.info("取消订单退还优惠卷参数couponRequest:{}", JSON.toJSONString(couponIdList));
                orderCouponUseService.orderReturnCoupon(couponRequest);
            }

        }

    }

}

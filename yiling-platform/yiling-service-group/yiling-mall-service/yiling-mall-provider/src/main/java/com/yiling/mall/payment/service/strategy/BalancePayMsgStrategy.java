package com.yiling.mall.payment.service.strategy;

import java.util.Collections;
import java.util.Date;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.mall.payment.service.MsgStrategyService;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderFirstInfoApi;
import com.yiling.order.order.api.OrderPaymentMethodApi;
import com.yiling.order.order.api.PresaleOrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.PresaleOrderDTO;
import com.yiling.order.order.dto.request.CreateOrderPayMentMethodRequest;
import com.yiling.order.order.dto.request.UpdateOrderPaymentStatusRequest;
import com.yiling.order.order.dto.request.UpdatePresaleOrderRequest;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTradeTypeEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.payment.enums.AppOrderStatusEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.dto.PayOrderDTO;

import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/** 尾款支付回调
 * @author zhigang.guo
 * @date: 2022/10/12
 */
@Component
@Slf4j
public class BalancePayMsgStrategy implements MsgStrategyService<PayOrderDTO, TradeTypeEnum> {

    @DubboReference
    OrderApi orderApi;
    @DubboReference
    PresaleOrderApi presaleOrderApi;
    @DubboReference
    OrderPaymentMethodApi orderPaymentMethodApi;
    @DubboReference
    OrderFirstInfoApi  firstInfoApi;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;
    @Lazy
    @Autowired
    private BalancePayMsgStrategy _this;

    @Override
    public TradeTypeEnum getMsgTradeType() {

        return TradeTypeEnum.BALANCE;
    }

    /**
     * 处理数据
     * @param orderDTO
     * @param payOrderDTO
     * @return
     */
    @GlobalTransactional
    public MqMessageBO processData(OrderDTO orderDTO,PayOrderDTO payOrderDTO,OrderTradeTypeEnum orderTradeTypeEnum) {

        UpdateOrderPaymentStatusRequest request = new UpdateOrderPaymentStatusRequest();
        request.setOrderId(payOrderDTO.getAppOrderId());
        request.setOpUserId(payOrderDTO.getUserId());
        request.setPaymentTime(new Date());
        request.setPayWay(payOrderDTO.getPayWay());
        request.setPaySource(payOrderDTO.getPaySource());
        request.setPaymentStatus(PaymentStatusEnum.PAID.getCode());
        request.setPaymentTime(payOrderDTO.getPayDate());
        // 修改为支付完成
        orderApi.updatePaymentStatus(request);

        CreateOrderPayMentMethodRequest createOrderPayMentMethodRequest = PojoUtils.map(orderDTO,CreateOrderPayMentMethodRequest.class);
        createOrderPayMentMethodRequest.setTradeType(orderTradeTypeEnum.getCode());
        createOrderPayMentMethodRequest.setPayChannel(payOrderDTO.getPayWay());
        createOrderPayMentMethodRequest.setPaySource(payOrderDTO.getPaySource());
        createOrderPayMentMethodRequest.setPaymentTime(payOrderDTO.getPayDate());
        createOrderPayMentMethodRequest.setPaymentAmount(payOrderDTO.getPayAmount());
        createOrderPayMentMethodRequest.setOrderId(orderDTO.getId());

        // 保存支付方式
        orderPaymentMethodApi.save(createOrderPayMentMethodRequest);

        UpdatePresaleOrderRequest updatePresaleOrderRequest = new UpdatePresaleOrderRequest();
        updatePresaleOrderRequest.setOrderId(payOrderDTO.getAppOrderId());

        // 修改付款支付方式
        switch (orderTradeTypeEnum) {
            case BALANCE:
                updatePresaleOrderRequest.setIsPayBalance(1);
                break;
            case DEPOSIT:
                updatePresaleOrderRequest.setIsPayDeposit(1);
                break;
            default: break;
        }

        // 修改预售订单已支付成功
        Boolean flag =  presaleOrderApi.updatePresalOrderByOrderId(updatePresaleOrderRequest);

        if (flag) {
            flag =  orderApi.updateOrderStatus(payOrderDTO.getAppOrderId(), OrderStatusEnum.UNAUDITED,OrderStatusEnum.UNDELIVERED,0l,"");
        }

        // 判断是否为新客
        if (flag) {
            // 判断是否新客，新客下单插入首单信息
            Boolean checkResult = firstInfoApi.checkNewVisitor(orderDTO.getBuyerEid(), OrderTypeEnum.getByCode(orderDTO.getOrderType()));
            if (checkResult) {
                flag =  firstInfoApi.saveFirstInfo(orderDTO.getId(),0l);
            }
        } else {

            // 回滚事务
            throw new RuntimeException("尾款支付,回调失败!");
        }

        if (flag) {
            // 通知流向埋点
            MqMessageBO orderErpPushBo  = new MqMessageBO(Constants.TOPIC_ORDER_PUSH_ERP, Constants.TAG_ORDER_PUSH_ERP, JSON.toJSONString(Collections.singleton(orderDTO.getId())));
            orderErpPushBo = mqMessageSendApi.prepare(orderErpPushBo);

            return orderErpPushBo;
        }

        // 回滚事务
        throw new RuntimeException("尾款支付,回调失败!");

    }

    @Override
    public MqAction processMessage(PayOrderDTO payOrderDTO) {
        if (log.isDebugEnabled()) {

            log.debug("销售订单支付尾款回调:{}", payOrderDTO);
        }

        // 支付失败不用修改状态
        if (AppOrderStatusEnum.SUCCESS !=  AppOrderStatusEnum.getByCode(payOrderDTO.getAppOrderStatus())) {

            return MqAction.CommitMessage;
        }

        OrderDTO orderDTO = orderApi.getOrderInfo(payOrderDTO.getAppOrderId());
        orderDTO.setPaySource(payOrderDTO.getPaySource());

        // 添加判断，防止消息重复消费
        if (orderDTO == null || PaymentStatusEnum.PAID == PaymentStatusEnum.getByCode(orderDTO.getPaymentStatus())) {

            log.warn("订单支付重复回调");
            return MqAction.CommitMessage;
        }

        PresaleOrderDTO orderInfo = presaleOrderApi.getOrderInfo(payOrderDTO.getAppOrderId());
        // 不是预售订单无需处理
        if (orderInfo == null) {
            return MqAction.CommitMessage;
        }

        if (orderInfo.getIsPayDeposit() != 1) {

            log.error("尾款回调订单,未支付定金!");

            return MqAction.ReconsumeLater;
        }

        MqMessageBO mqMessageBO = _this.processData(orderDTO, payOrderDTO,OrderTradeTypeEnum.BALANCE);

        if (mqMessageBO != null) {

            mqMessageSendApi.send(mqMessageBO);
        }

        return MqAction.CommitMessage;
    }
}

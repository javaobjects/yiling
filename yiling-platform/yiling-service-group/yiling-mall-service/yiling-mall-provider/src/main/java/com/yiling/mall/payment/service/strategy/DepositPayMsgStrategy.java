package com.yiling.mall.payment.service.strategy;

import java.util.Date;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.mall.payment.service.MsgStrategyService;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderPaymentMethodApi;
import com.yiling.order.order.api.PresaleOrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.PresaleOrderDTO;
import com.yiling.order.order.dto.request.CreateOrderPayMentMethodRequest;
import com.yiling.order.order.dto.request.UpdateOrderPaymentStatusRequest;
import com.yiling.order.order.dto.request.UpdatePresaleOrderRequest;
import com.yiling.order.order.enums.OrderTradeTypeEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.order.enums.PreSaleActivityTypeEnum;
import com.yiling.payment.enums.AppOrderStatusEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.dto.PayOrderDTO;

import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/** 定金支付回调
 * @author zhigang.guo
 * @date: 2022/10/12
 */
@Component
@Slf4j
public class DepositPayMsgStrategy implements MsgStrategyService<PayOrderDTO, TradeTypeEnum> {

    @DubboReference
    OrderApi orderApi;
    @DubboReference
    PresaleOrderApi presaleOrderApi;
    @DubboReference
    OrderPaymentMethodApi orderPaymentMethodApi;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;
    @Lazy
    @Autowired
    private DepositPayMsgStrategy _this;
    @Autowired
    private BalancePayMsgStrategy balancePayMsgStrategy;

    @Override
    public TradeTypeEnum getMsgTradeType() {

        return TradeTypeEnum.DEPOSIT;
    }

    /**
     * 处理数据
     * @param orderDTO
     * @param payOrderDTO
     * @return
     */
    @GlobalTransactional
    public MqAction processData(OrderDTO orderDTO,PayOrderDTO payOrderDTO) {

        UpdateOrderPaymentStatusRequest request = new UpdateOrderPaymentStatusRequest();
        request.setOrderId(payOrderDTO.getAppOrderId());
        request.setOpUserId(payOrderDTO.getUserId());
        request.setPaymentTime(new Date());
        request.setPayWay(payOrderDTO.getPayWay());
        request.setPaySource(payOrderDTO.getPaySource());
        request.setPaymentStatus(PaymentStatusEnum.PARTPAID.getCode());
        // 修改为部分支付
        orderApi.updatePaymentStatus(request);

        CreateOrderPayMentMethodRequest createOrderPayMentMethodRequest = PojoUtils.map(orderDTO,CreateOrderPayMentMethodRequest.class);
        createOrderPayMentMethodRequest.setTradeType(OrderTradeTypeEnum.DEPOSIT.getCode());
        createOrderPayMentMethodRequest.setPayChannel(payOrderDTO.getPayWay());
        createOrderPayMentMethodRequest.setPaySource(payOrderDTO.getPaySource());
        createOrderPayMentMethodRequest.setPaymentTime(payOrderDTO.getPayDate());
        createOrderPayMentMethodRequest.setPaymentAmount(payOrderDTO.getPayAmount());
        createOrderPayMentMethodRequest.setOrderId(orderDTO.getId());

        // 保存支付方式
        orderPaymentMethodApi.save(createOrderPayMentMethodRequest);


        UpdatePresaleOrderRequest updatePresaleOrderRequest = new UpdatePresaleOrderRequest();
        updatePresaleOrderRequest.setOrderId(payOrderDTO.getAppOrderId());
        updatePresaleOrderRequest.setIsPayDeposit(1);

        // 修改预售订单已支付成功
        Boolean flag =  presaleOrderApi.updatePresalOrderByOrderId(updatePresaleOrderRequest);

        if (flag) {

            return MqAction.CommitMessage;
        }

        // 回滚事务
        throw new RuntimeException("定金支付,回调失败!");

    }

    @Override
    public MqAction processMessage(PayOrderDTO payOrderDTO) {
        if (log.isDebugEnabled()) {

            log.debug("销售订单支付定金回调:{}", payOrderDTO);
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
        // 支付定金已部分支付无需处理
        if (PaymentStatusEnum.PARTPAID == PaymentStatusEnum.getByCode(orderDTO.getPaymentStatus())) {

            return MqAction.CommitMessage;
        }

        PresaleOrderDTO orderInfo = presaleOrderApi.getOrderInfo(payOrderDTO.getAppOrderId());
        if (orderInfo == null) {

            return MqAction.CommitMessage;
        }

        switch (PreSaleActivityTypeEnum.getByCode(orderInfo.getActivityType())) {
            case FULL:
                // 全款预售走定金支付活动时触发,尾款支付逻辑
                MqMessageBO mqMessageBO = balancePayMsgStrategy.processData(orderDTO, payOrderDTO, OrderTradeTypeEnum.DEPOSIT);
                if (mqMessageBO != null) {

                    mqMessageSendApi.send(mqMessageBO);
                }
                return MqAction.CommitMessage;

                // 定金预售活动
            default:
                return _this.processData(orderDTO,payOrderDTO);
        }
    }
}

package com.yiling.mall.payment.service.strategy;

import java.util.Date;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.mall.payment.api.PaymentDaysOrderApi;
import com.yiling.mall.payment.service.MsgStrategyService;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.request.UpdateOrderPaymentStatusRequest;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.dto.PayOrderDTO;
import com.yiling.settlement.b2b.api.SettlementOrderSyncApi;

import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/** 账期支付回调
 * @author zhigang.guo
 * @date: 2022/9/15
 */
@Component
@Slf4j
public class PaymentDaysOrderMsgStrategy  implements MsgStrategyService<PayOrderDTO,TradeTypeEnum> {

    @DubboReference
    OrderApi orderApi;
    @DubboReference
    PaymentDaysOrderApi paymentDaysOrderApi;
    @DubboReference
    SettlementOrderSyncApi settlementOrderSyncApi;
    @Lazy
    @Autowired
    private PaymentDaysOrderMsgStrategy _this;

    @Override
    public TradeTypeEnum getMsgTradeType() {

        return TradeTypeEnum.BACK;
    }


    /**
     * 处理数据，加上全局事务
     * @param orderDTO
     * @param payOrderDTO
     * @return
     */
    @GlobalTransactional
    public MqAction processData(OrderDTO orderDTO,PayOrderDTO payOrderDTO) {

        boolean isSuccess = paymentDaysOrderApi.updatePaymentOrderAmount(payOrderDTO.getAppOrderId());

        if (!isSuccess) {

            return MqAction.ReconsumeLater;
        }

        // 通知结算系统，通知该订单记录已在线还款成功
        boolean settlementNotifyResult = settlementOrderSyncApi.cashRepaymentNotifySett(orderDTO.getOrderNo());

        // 账期结算通知结果
        if (!settlementNotifyResult) {

            throw new RuntimeException("调用结算系统，还款结算异常!");
        }

        UpdateOrderPaymentStatusRequest request = new UpdateOrderPaymentStatusRequest();
        request.setOrderId(payOrderDTO.getAppOrderId());
        request.setOpUserId(payOrderDTO.getUserId());
        request.setPaymentTime(new Date());
        request.setPayWay(payOrderDTO.getPayWay());
        request.setPaySource(payOrderDTO.getPaySource());
        request.setPaymentStatus(PaymentStatusEnum.PAID.getCode());
        orderApi.updatePaymentStatus(request);

        return MqAction.CommitMessage;

    }


    @Override
    public MqAction processMessage(PayOrderDTO payOrderDTO) {

        if (log.isDebugEnabled()) {

            log.debug("账期支付还款回调:{}", payOrderDTO);
        }

        // 添加判断，防止消息重复消费
        OrderDTO orderDTO = orderApi.getOrderInfo(payOrderDTO.getAppOrderId());

        if (orderDTO == null || PaymentStatusEnum.PAID == PaymentStatusEnum.getByCode(orderDTO.getPaymentStatus())) {

            log.warn("订单支付重复回调");

            return MqAction.CommitMessage;
        }

        return _this.processData(orderDTO,payOrderDTO);
    }


}

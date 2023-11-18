package com.yiling.mall.order.mq.message.listener;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.mall.order.service.PresaleOrderService;
import com.yiling.order.order.enums.PreSalOrderReminderTypeEnum;

import lombok.extern.slf4j.Slf4j;

/** 预售订单在线支付自动取消短信提醒
 * @author zhigang.guo
 * @date: 2022/10/10
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_ORDER_CANCEL_SMS_NOTIFY, consumerGroup = Constants.TOPIC_ORDER_CANCEL_SMS_NOTIFY)
public class PresaleOrderCancelMessageListener extends AbstractMessageListener {

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;
    @Autowired
    PresaleOrderService presaleOrderService;


    @Override
    @MdcLog
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {

        // 订单编号
        String orderNo = body;
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), orderNo);

        boolean orderSmsNotice = presaleOrderService.sendPresaleOrderSmsNotice(orderNo, PreSalOrderReminderTypeEnum.CANCEL_REMINDER);

        if (!orderSmsNotice) {

           return MqAction.ReconsumeLater;
        }

        return MqAction.CommitMessage;
    }

    @Override
    protected int getMaxReconsumeTimes() {

        return 3;
    }

    @Override
    protected IConsumeFailureHandler getConsumeFailureHandler() {

        return (body, message, context, e) -> {
            mqMessageConsumeFailureApi.handleConsumeFailure(body, message, e);
            DubboUtils.quickAsyncCall("mqMessageConsumeFailureApi", "handleConsumeFailure");
        };
    }
}

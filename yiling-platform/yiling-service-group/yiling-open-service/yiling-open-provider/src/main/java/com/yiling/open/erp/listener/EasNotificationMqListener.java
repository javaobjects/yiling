package com.yiling.open.erp.listener;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.open.erp.handler.EasNotificationHandler;
import com.yiling.open.webservice.inform.WSPopToEasWebserviceFacadeSrvProxyServiceLocator;
import com.yiling.open.webservice.login.EASLoginProxyServiceLocator;

import lombok.extern.slf4j.Slf4j;

/**
 * 消息消费监听
 *
 * @author shuan
 */
@Slf4j
@RocketMqListener(topic = "eas_notification", consumerGroup = "eas_notification")
public class EasNotificationMqListener extends AbstractMessageListener {

    @Autowired
    private EASLoginProxyServiceLocator                      easLoginProxyServiceLocator;
    @Autowired
    private WSPopToEasWebserviceFacadeSrvProxyServiceLocator wsPopToEasWebserviceFacadeSrvProxyServiceLocator;
    @Autowired
    private EasNotificationHandler                           easNotificationHandler;
    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
            easNotificationHandler.executeEas(msg);
            return MqAction.CommitMessage;
        } catch (Exception e) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg, e);
            throw new RuntimeException(e);
        }
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

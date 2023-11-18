package com.yiling.sjms.gb.listener;

import org.apache.commons.lang3.StringUtils;
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
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.sjms.gb.handler.GbOrderHandler;

import lombok.extern.slf4j.Slf4j;


/**
 * 接受团购数据审核通过的变更数据
 * @author shuan
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_FLOW_SALE_GB_APPROVED_TASK, consumerGroup = Constants.TAG_FLOW_SALE_GB_APPROVED_TASK, maxThread = 1)
public class GbOrderApprovedListener extends AbstractMessageListener {
    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @Autowired
    private GbOrderHandler gbOrderHandler;

    @Override
    @MdcLog
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);

        if (StringUtils.isBlank(msg)) {
            log.warn("消息为空, MsgId:{}, Topic:{}", message.getMsgId(), message.getTopic());
            return MqAction.CommitMessage;
        }
        Long formId = Long.parseLong(msg);
        if (formId.intValue() == 0) {
            log.warn("消息的formId为0, MsgId:{}, Topic:{}", message.getMsgId(), message.getTopic());
            return MqAction.CommitMessage;
        }

        // 保存、更新团购审批通过数据
        boolean result = gbOrderHandler.gbOrderApproved(formId);
        if (result) {
            return MqAction.CommitMessage;
        }
        return MqAction.ReconsumeLater;
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

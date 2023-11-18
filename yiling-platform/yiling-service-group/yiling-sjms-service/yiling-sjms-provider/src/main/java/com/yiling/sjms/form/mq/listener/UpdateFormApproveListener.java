package com.yiling.sjms.form.mq.listener;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.sjms.form.service.strategy.FormStrategyContext;
import com.yiling.sjms.gb.dto.request.UpdateGBFormInfoRequest;

import cn.hutool.core.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 审批通过
 * @author: gxl
 * @date: 2023/2/27
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_CHANGE_FORM_APPROVE, consumerGroup = Constants.TOPIC_CHANGE_FORM_APPROVE)
public class UpdateFormApproveListener extends AbstractMessageListener {
    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @Autowired
    FormStrategyContext formStrategyContext;
    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), CharsetUtil.UTF_8);
        log.info("UpdateFormApproveListener MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        UpdateGBFormInfoRequest updateGBFormInfoRequest = JSON.parseObject(msg, UpdateGBFormInfoRequest.class);
        formStrategyContext.taskProgressHandlerMap.get(updateGBFormInfoRequest.getFormType()).approve(updateGBFormInfoRequest);
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
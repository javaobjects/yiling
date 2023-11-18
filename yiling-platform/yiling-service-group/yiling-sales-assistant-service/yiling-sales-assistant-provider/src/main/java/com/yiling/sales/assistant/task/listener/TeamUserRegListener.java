package com.yiling.sales.assistant.task.listener;

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
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.sales.assistant.task.dto.request.UpdateUserTaskNoLimitRequest;
import com.yiling.sales.assistant.task.service.UserTaskService;

import cn.hutool.core.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 拉新人任务
 * @author: ray
 * @date: 2022/1/20
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_SALES_INVITE_REGISTER_SUCCESS_NOTIFY, consumerGroup = Constants.TOPIC_SALES_INVITE_REGISTER_SUCCESS_NOTIFY)
public class TeamUserRegListener extends AbstractMessageListener {
    @Autowired
    private UserTaskService userTaskService;
    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;
    @MdcLog
    @Override
    public MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), CharsetUtil.UTF_8);
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        UpdateUserTaskNoLimitRequest updateUserTaskNoLimitRequest = JSON.parseObject(msg, UpdateUserTaskNoLimitRequest.class);
        userTaskService.updateInviteUserProgress(updateUserTaskNoLimitRequest);
        return   MqAction.CommitMessage;
    }

    @Override
    protected int getMaxReconsumeTimes() {
        return 0;
    }

    @Override
    protected IConsumeFailureHandler getConsumeFailureHandler() {
        return null;
    }
}
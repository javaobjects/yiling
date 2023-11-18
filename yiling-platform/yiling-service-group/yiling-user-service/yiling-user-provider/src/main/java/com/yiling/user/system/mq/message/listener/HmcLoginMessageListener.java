package com.yiling.user.system.mq.message.listener;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.user.common.util.Constants;
import com.yiling.user.system.service.HmcUserService;

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;

/**
 * HMC登录消息监听器 <br/>
 *
 * @author: xuan.zhou
 * @date: 2022/3/28
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_HMC_LOGIN, consumerGroup = Constants.TOPIC_HMC_LOGIN+"111")
public class HmcLoginMessageListener extends AbstractMessageListener {

    @Autowired
    HmcUserService hmcUserService;
    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        log.info("[HmcLoginMessageListener]收到消息:{}", body);
        // 用户ID
        Long userId = Convert.toLong(body);

        // 更新用户登录次数、最后登录时间
        hmcUserService.updateLoginTime(userId);

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
        };
    }
}

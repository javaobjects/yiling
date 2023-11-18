package com.yiling.basic.mq.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.entity.MqMessageConsumeFailureDO;
import com.yiling.basic.mq.service.MqMessageConsumeFailureService;

import cn.hutool.core.util.StrUtil;

/**
 * mq消费失败的消息 API 实现
 *
 * @author: xuan.zhou
 * @date: 2022/1/26
 */
@DubboService
public class MqMessageConsumeFailureApiImpl implements MqMessageConsumeFailureApi {

    @Autowired
    private MqMessageConsumeFailureService mqMessageConsumeFailureService;

    @Override
    public void handleConsumeFailure(String body, MessageExt message, ConsumeConcurrentlyContext context, Exception e) {
        this.handleConsumeFailure(body, message, e);
    }

    @Override
    public void handleConsumeFailure(String body, MessageExt message, Exception e) {
        MqMessageConsumeFailureDO entity = new MqMessageConsumeFailureDO();
        entity.setMsgId(message.getMsgId());
        entity.setTopic(message.getTopic());
        entity.setTags(message.getTags());
        entity.setKeys(message.getKeys());
        entity.setBody(body);
        entity.setFailedMsg(StrUtil.sub(e.getLocalizedMessage(), 0, 1000));
        mqMessageConsumeFailureService.save(entity);
    }
}

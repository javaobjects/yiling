package com.yiling.sjms.oa.todo.mq.listener;

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
import com.yiling.sjms.oa.todo.dto.request.ProcessDoneRequest;
import com.yiling.sjms.oa.todo.service.OaTodoService;

import cn.hutool.core.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * oa待办消息
 * @author: gxl
 * @date: 2023/1/9
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_OA_TODO_DONE, consumerGroup = Constants.TOPIC_OA_TODO_DONE,tag = Constants.TAG_OA_TODO_DONE)
public class OaTodoDoneListener extends AbstractMessageListener {

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;
    @Autowired
    OaTodoService oaTodoService;

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), CharsetUtil.UTF_8);
        log.info("OaTodoDoneListener MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), body);
        ProcessDoneRequest processDoneRequest = JSON.parseObject(msg, ProcessDoneRequest.class);
        Boolean aBoolean = oaTodoService.processDone(processDoneRequest);
        if(!aBoolean){
            log.error("oa待办消息改已办失败processDoneRequest={}",processDoneRequest.toString());
        }
        return   MqAction.CommitMessage;
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
package com.yiling.ih.question.mq.listener;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;

import com.yiling.ih.common.ApiResult;
import com.yiling.ih.question.feign.QuestionReplySendMessageFeignClient;
import com.yiling.ih.question.feign.dto.request.QuestionReplySendMessageRequest;


import lombok.extern.slf4j.Slf4j;

/**
 * HMC 订单完成消息监听器
 * 发送用药指导通知
 *
 * @author: fan.shen
 * @date: 2022/4/26
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_CMS_FIRST_REPLY_SEND, consumerGroup = Constants.TAG_CMS_FIRST_REPLY_SEND)
public class QuestionReplyFirstListener implements MessageListener {

    @Autowired
    private QuestionReplySendMessageFeignClient questionReplySendMessageFeignClient;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        QuestionReplySendMessageRequest request ;
        request = JSON.parseObject(MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8"), QuestionReplySendMessageRequest.class);

        try {
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), JSON.toJSONString(request));
            ApiResult apiResult = questionReplySendMessageFeignClient.questionReplySendMessage(request);
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，返回结果ApiResult:{}", message.getMsgId(), message.getTopic(), message.getTags(), JSON.toJSONString(apiResult));
        }catch (Exception e){
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(),message.getTopic(), message.getTags(),JSON.toJSONString(request),e);
            throw e;
        }
        return MqAction.CommitMessage;
    }
}

package com.yiling.dataflow.wash.listener;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.dataflow.wash.service.UnlockCustomerMatchingInfoService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/6/9
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_UNLOCK_CUSTOMER_MATCHING_RATE_BATCH_TASK, consumerGroup = Constants.TAG_UNLOCK_CUSTOMER_MATCHING_RATE_BATCH_TASK, maxThread = 3)
public class UnlockCustomerMatchingRateBatchListener extends AbstractMessageListener {

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @Autowired
    UnlockCustomerMatchingInfoService unlockCustomerMatchingInfoService;

    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg  = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        unlockCustomerMatchingInfoService.matchingRateBatchExecute();
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

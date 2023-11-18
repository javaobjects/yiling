package com.yiling.dataflow.wash.listener;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.dataflow.wash.dto.request.UpdateUnlockFlowWashSaleRequest;
import com.yiling.dataflow.wash.service.UnlockFlowWashSaleService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/5/10
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_UNLOCK_FLOW_WASH_SALE_CLASSIFICATION, consumerGroup = Constants.TAG_UNLOCK_FLOW_WASH_SALE_CLASSIFICATION,maxThread = 3)
public class UnlockFlowWashSaleClassificationSetListener extends AbstractMessageListener {

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @Autowired
    UnlockFlowWashSaleService unlockFlowWashSaleService;

    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg  = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        UpdateUnlockFlowWashSaleRequest request = JSON.parseObject(msg, UpdateUnlockFlowWashSaleRequest.class);
        unlockFlowWashSaleService.updateClassificationCrmIdAndCustomerName(request);
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

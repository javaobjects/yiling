package com.yiling.sjms.gb.listener;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.dataflow.gb.api.GbOrderApi;
import com.yiling.dataflow.gb.bo.GbFormReviewMessageBO;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.sjms.gb.api.GbFormApi;
import com.yiling.sjms.gb.handler.GbOrderHandler;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * 接受团购数据审核通过的变更数据
 * @author shuan
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_FLOW_SALE_GB_REVIEW_APPROVED_TASK, consumerGroup = Constants.TOPIC_FLOW_SALE_GB_REVIEW_APPROVED_TASK, maxThread = 1)
public class GbOrderReviewedListener extends AbstractMessageListener {
    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @Autowired
    private GbOrderHandler gbOrderHandler;

    @DubboReference
    private GbOrderApi gbOrderApi;
    @DubboReference
    private GbFormApi gbFormApi;

    @Override
    @MdcLog
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);

        if (StringUtils.isBlank(msg)) {
            log.warn("消息为空, MsgId:{}, Topic:{}", message.getMsgId(), message.getTopic());
            return MqAction.CommitMessage;
        }
        List<GbFormReviewMessageBO> list =  JSON.parseArray(msg, GbFormReviewMessageBO.class);
        if (CollUtil.isEmpty(list)) {
            log.warn("团购复核通知数据为空");
            return MqAction.CommitMessage;
        }

        // 保存、更新团购复核通过数据
        boolean result = gbOrderHandler.gbOrderReviewed(list);
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

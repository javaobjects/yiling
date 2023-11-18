package com.yiling.sjms.gb.listener;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.dataflow.gb.api.GbOrderApi;
import com.yiling.dataflow.gb.dto.GbOrderDTO;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;

import lombok.extern.slf4j.Slf4j;


/**
 * 团购数据匹配原流向数据
 *
 * @author shuan
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_FLOW_SALE_GB_MATE_TASK, consumerGroup = Constants.TAG_FLOW_SALE_GB_MATE_TASK, maxThread = 1)
public class GbOrderMateFlowSaleListener extends AbstractMessageListener {
    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @DubboReference
    private GbOrderApi gbOrderApi;

    @Override
    @MdcLog
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);

        if (StringUtils.isBlank(msg)) {
            log.error("消息为空");
            return MqAction.CommitMessage;
        }
        //gb_order表里面id
        Long id = Long.parseLong(msg);
        GbOrderDTO gbOrderDTO=gbOrderApi.getById(id);
        if(gbOrderDTO==null){
            log.error("查询团购主数据对象为空");
            return MqAction.CommitMessage;
        }
        if(!gbOrderDTO.getExecStatus().equals(1)){
            log.error("查询团购主数据状态不正确");
            return MqAction.CommitMessage;
        }
        gbOrderApi.mateFlow(id);
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

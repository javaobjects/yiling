package com.yiling.workflow.workflow.mq.listener;

import java.util.Objects;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;

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
import com.yiling.workflow.workflow.api.ProcessApi;
import com.yiling.workflow.workflow.dto.request.StartGroupBuyRequest;

import cn.hutool.core.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 发起团购提报流程
 * @author: gxl
 * @date: 2022/12/8
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_GB_SUBMIT_SEND_WORKFLOW, consumerGroup = Constants.TOPIC_GB_SUBMIT_SEND_WORKFLOW,tag = Constants.TAG_GB_SUBMIT_SEND_WORKFLOW)
public class StartProcessListener  extends AbstractMessageListener {

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @DubboReference
    ProcessApi processApi;

    @Resource
    protected HistoryService historyService;

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), CharsetUtil.UTF_8);
        log.info("StartProcessListener MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        StartGroupBuyRequest request = JSON.parseObject(msg, StartGroupBuyRequest.class);
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(request.getBusinessKey()).singleResult();
        if(Objects.nonNull(historicProcessInstance)){
            log.info("此流程已存在businessKey={}",request.getBusinessKey());
            return   MqAction.CommitMessage;
        }
        processApi.startGroupBuyProcess(request);
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
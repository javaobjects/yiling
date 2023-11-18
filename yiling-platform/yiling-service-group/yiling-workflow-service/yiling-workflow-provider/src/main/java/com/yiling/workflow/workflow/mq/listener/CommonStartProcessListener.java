package com.yiling.workflow.workflow.mq.listener;

import java.util.Objects;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.workflow.workflow.dto.request.StartProcessRequest;
import com.yiling.workflow.workflow.dto.request.WorkFlowBaseRequest;
import com.yiling.workflow.workflow.service.WfProcessService;

import cn.hutool.core.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 通用发起流程监听
 * @author: gxl
 * @date: 2023/2/28
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_SUBMIT_SEND_WORKFLOW, consumerGroup = Constants.TOPIC_SUBMIT_SEND_WORKFLOW)
public class CommonStartProcessListener extends AbstractMessageListener {
    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;
    @Autowired
    WfProcessService wfProcessService;

    @Resource
    protected HistoryService historyService;
    @Resource
    protected IdentityService identityService;

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), CharsetUtil.UTF_8);
        log.info("CommonStartProcessListener MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        WorkFlowBaseRequest request = JSON.parseObject(msg, WorkFlowBaseRequest.class);
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(request.getBusinessKey()).singleResult();
        if(Objects.nonNull(historicProcessInstance)){
            log.info("此流程已存在businessKey={}",request.getBusinessKey());
            return   MqAction.CommitMessage;
        }
        StartProcessRequest startProcessRequest = new StartProcessRequest();
        PojoUtils.map(request,startProcessRequest);
        identityService.setAuthenticatedUserId(request.getStartUserId());
        wfProcessService.startProcess(startProcessRequest);
        identityService.setAuthenticatedUserId(null);
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
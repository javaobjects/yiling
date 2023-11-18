package com.yiling.workflow.workflow.mq.listener;

import java.util.Map;

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
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.workflow.workflow.constant.FlowConstant;
import com.yiling.workflow.workflow.dto.request.StartGroupBuyCancleRequest;
import com.yiling.workflow.workflow.dto.request.StartProcessRequest;
import com.yiling.workflow.workflow.service.WfProcessService;

import cn.hutool.core.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 团购取消发起审批流程
 * @author: gxl
 * @date: 2022/12/28
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_GB_CANCEL_SEND_WORKFLOW, consumerGroup = Constants.TOPIC_GB_CANCEL_SEND_WORKFLOW,tag = Constants.TAG_GB_CANCEL_SEND_WORKFLOW)
public class StartCancleProcessListener extends AbstractMessageListener {
    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @Autowired
    private WfProcessService wfProcessService;
    @Resource
    protected IdentityService identityService;
    @Resource
    protected HistoryService historyService;

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {

        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), CharsetUtil.UTF_8);
        log.info("StartCancleProcessListener MsgId:{}, 团购取消MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        StartGroupBuyCancleRequest request = JSON.parseObject(msg, StartGroupBuyCancleRequest.class);
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().includeProcessVariables().processInstanceBusinessKey(request.getSrcGbNo()).finished().singleResult();
        StartProcessRequest start = new StartProcessRequest();
        //设置发起人
        identityService.setAuthenticatedUserId(historicProcessInstance.getStartUserId());
        Map<String, Object> processVariables = historicProcessInstance.getProcessVariables();
        processVariables.put(FlowConstant.GB_ID,request.getGbId());
        processVariables.put(FlowConstant.FORM_ID,request.getGbId());
        processVariables.put("previousAssignee","");
        start.setBusinessKey(request.getGbNo()).setVariables(processVariables).setStartUserId(historicProcessInstance.getStartUserId());
        start.setFormType(FormTypeEnum.GB_CANCEL.getCode());
        wfProcessService.startCancleProcess(start);
        identityService.setAuthenticatedUserId(null);
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
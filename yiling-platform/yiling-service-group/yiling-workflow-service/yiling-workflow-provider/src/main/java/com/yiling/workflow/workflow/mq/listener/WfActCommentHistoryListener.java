package com.yiling.workflow.workflow.mq.listener;

import java.util.Date;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.oa.todo.api.OaTodoApi;
import com.yiling.sjms.oa.todo.dto.request.ProcessDoneRequest;
import com.yiling.workflow.workflow.constant.FlowConstant;
import com.yiling.workflow.workflow.entity.ActHiTaskinstDO;
import com.yiling.workflow.workflow.entity.ActRuExecutionDO;
import com.yiling.workflow.workflow.entity.ActRuTaskDO;
import com.yiling.workflow.workflow.entity.WfActHistoryDO;
import com.yiling.workflow.workflow.service.ActHiTaskinstService;
import com.yiling.workflow.workflow.service.ActRuExecutionService;
import com.yiling.workflow.workflow.service.ActRuTaskService;
import com.yiling.workflow.workflow.service.WfActHistoryService;

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;

/**
 * 流程批注操作历史记录消费
 *
 * @author: xuan.zhou
 * @date: 2023/2/15
 */
@Slf4j
@RocketMqListener(topic = FlowConstant.TOPIC_WF_ACT_COMMENT, consumerGroup = FlowConstant.TOPIC_WF_ACT_COMMENT)
public class WfActCommentHistoryListener extends AbstractMessageListener {

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;
    @DubboReference
    OaTodoApi oaTodoApi;
    @DubboReference
    FormApi formApi;

    @Autowired
    WfActHistoryService wfActHistoryService;

    @Autowired
    ActRuTaskService actRuTaskService;
    @Autowired
    HistoryService historyService;
    @Autowired
    ActHiTaskinstService actHiTaskinstService;

    @Autowired
    ActRuExecutionService actRuExecutionService;

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        Long id = Convert.toLong(body.split("-")[0], 0L);
        String empId = body.split("-")[1];

        WfActHistoryDO wfActHistoryDO = wfActHistoryService.getById(id);
        if (wfActHistoryDO == null) {
            log.error("未找到转发操作历史记录。id={}", id);
            return MqAction.CommitMessage;
        }

        FormDTO formDTO = formApi.getById(wfActHistoryDO.getFormId());

        ProcessDoneRequest request = new ProcessDoneRequest();
        request.setBizId(formDTO.getCode());
        request.setTitle(formDTO.getName());
        request.setWorkflowName(formDTO.getFlowTplName());
        request.setReceiverCode(empId);
        oaTodoApi.processDone(request);
        if(wfActHistoryDO.getForwardHistoryId()>0){
            LambdaQueryWrapper<ActRuTaskDO> wrapper = Wrappers.lambdaQuery();
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(formDTO.getCode()).singleResult();
            wrapper.eq(ActRuTaskDO::getAssignee,empId).eq(ActRuTaskDO::getProcInstId,historicProcessInstance.getId()).eq(ActRuTaskDO::getExecutionId,wfActHistoryDO.getForwardHistoryId()).last("limit 1");
            ActRuTaskDO actRuTaskDO = actRuTaskService.getOne(wrapper);
            actRuTaskService.removeById(actRuTaskDO.getId());
            ActRuExecutionDO actRuExecutionDO = new ActRuExecutionDO();
            actRuExecutionDO.setId(wfActHistoryDO.getForwardHistoryId().toString()+"-"+empId).setParentId(null);
            actRuExecutionService.updateById(actRuExecutionDO);
            actRuExecutionService.removeById(wfActHistoryDO.getForwardHistoryId().toString()+"-"+empId);

            ActHiTaskinstDO actHiTaskinstDO = new ActHiTaskinstDO();
            actHiTaskinstDO.setId(actRuTaskDO.getId());
            actHiTaskinstDO.setEndTime(new Date()).setLastUpdatedTime(new Date());
            actHiTaskinstService.updateById(actHiTaskinstDO);
        }

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

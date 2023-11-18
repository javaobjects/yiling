package com.yiling.workflow.workflow.listener;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.flowable.engine.HistoryService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.util.Constants;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.gb.api.GbFormApi;
import com.yiling.sjms.gb.dto.request.UpdateGBFormInfoRequest;
import com.yiling.sjms.oa.todo.dto.request.ProcessOverRequest;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.workflow.workflow.constant.FlowConstant;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 任务结束监听器
 * @author: gxl
 * @date: 2022/12/1
 */
@Slf4j
@Component(value = "processEndListener")
public class ProcessEndListener implements JavaDelegate {
    @Resource
    protected HistoryService historyService;
    @DubboReference
    private GbFormApi gbFormApi;
    @DubboReference
    private MqMessageSendApi mqMessageSendApi;
    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    private FormApi formApi;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        long formId = (long)delegateExecution.getVariable(FlowConstant.FORM_ID);
        log.info("任务审批完结formID={}",formId);
        UpdateGBFormInfoRequest request = new UpdateGBFormInfoRequest();
        request.setId(formId);
        request.setOriginalStatus(FormStatusEnum.AUDITING).setNewStatus(FormStatusEnum.APPROVE).setApproveTime(new Date());
        FormDTO formDTO = formApi.getById(formId);
        request.setFormType(formDTO.getType());
        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_CHANGE_FORM_APPROVE,Constants.TAG_CHANGE_FORM_APPROVE, JSON.toJSONString(request) );
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        mqMessageSendApi.send(mqMessageBO);
        ProcessOverRequest processDoneRequest = new ProcessOverRequest();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(delegateExecution.getProcessInstanceId()).singleResult();
        EsbEmployeeDTO employeeDTO = esbEmployeeApi.getByEmpId(historicProcessInstance.getStartUserId());
        String title = historicProcessInstance.getProcessDefinitionName()+"-"+employeeDTO.getEmpName()+"-"+ DateUtil.format(historicProcessInstance.getStartTime(), DatePattern.NORM_DATE_PATTERN)+"("+historicProcessInstance.getBusinessKey()+")";
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(historicProcessInstance.getId()).list();
        processDoneRequest.setWorkflowName(historicProcessInstance.getProcessDefinitionName()).setTitle(title).setReceiverCode(historicProcessInstance.getStartUserId()).setBizId(list.get(0).getId());
        MqMessageBO nextMqMessageBO = new MqMessageBO(Constants.TOPIC_OA_TODO_OVER, Constants.TAG_OA_TODO_OVER, JSON.toJSONString(processDoneRequest) );
        nextMqMessageBO = mqMessageSendApi.prepare(nextMqMessageBO);
        mqMessageSendApi.send(nextMqMessageBO);
    }
}
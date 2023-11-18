package com.yiling.workflow.workflow.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.util.ExecutionGraphUtil;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.basic.sms.api.SmsApi;
import com.yiling.basic.sms.enums.SmsTypeEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.gb.api.GbFormApi;
import com.yiling.sjms.gb.dto.request.UpdateGBFormInfoRequest;
import com.yiling.sjms.oa.todo.dto.request.ProcessDoneRequest;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.workflow.factory.FlowServiceFactory;
import com.yiling.workflow.workflow.constant.FlowConstant;
import com.yiling.workflow.workflow.dto.CommentDTO;
import com.yiling.workflow.workflow.dto.WfActHistoryDTO;
import com.yiling.workflow.workflow.dto.WfTaskDTO;
import com.yiling.workflow.workflow.dto.WorkFlowNodeDTO;
import com.yiling.workflow.workflow.dto.request.AddCommentHistoryRequest;
import com.yiling.workflow.workflow.dto.request.AddForwardHistoryRequest;
import com.yiling.workflow.workflow.dto.request.AddWfActHistoryRequest;
import com.yiling.workflow.workflow.dto.request.CommonResubmitRequest;
import com.yiling.workflow.workflow.dto.request.CompleteWfTaskRequest;
import com.yiling.workflow.workflow.dto.request.GetTaskDetailRequest;
import com.yiling.workflow.workflow.enums.FlowCommentEnum;
import com.yiling.workflow.workflow.enums.WfActTypeEnum;
import com.yiling.workflow.workflow.service.WfActHistoryService;
import com.yiling.workflow.workflow.service.WfProcessService;
import com.yiling.workflow.workflow.service.WfTaskService;
import com.yiling.workflow.workflow.util.FlowUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

/**
 * 任务
 *
 * @author: gxl
 * @date: 2022/11/30
 */
@Slf4j
@Service
public class WfTaskServiceImpl extends FlowServiceFactory implements WfTaskService {

    @Autowired
    private FlowUtils flowUtils;

    @Autowired
    private WfActHistoryService wfActHistoryService;
    @Autowired
    private WfProcessService wfProcessService;

    @DubboReference
    private GbFormApi gbFormApi;

    @DubboReference
    private FormApi formApi;

    @DubboReference
    private MqMessageSendApi mqMessageSendApi;

    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    private SmsApi smsApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeTask(CompleteWfTaskRequest request) {
        Task task = taskService.createTaskQuery().taskId(request.getTaskId()).singleResult();
        if (Objects.isNull(task)) {
            throw new BusinessException(ResultCode.FAILED, "任务不存在");
        }
        if (task.isSuspended()) {
            throw new BusinessException(ResultCode.FAILED, "任务处于挂起状态");
        }
        AddWfActHistoryRequest addWfActHistoryRequest = new AddWfActHistoryRequest();
        addWfActHistoryRequest.setFormId(request.getGbId()).setFromEmpId(request.getAssignee()).setType(WfActTypeEnum.APPROVE.getCode()).setText(request.getComment());
        wfActHistoryService.addActHistory(addWfActHistoryRequest);
        taskService.addComment(request.getTaskId(), task.getProcessInstanceId(), FlowCommentEnum.PASS.getType(), request.getComment());
        taskService.setAssignee(request.getTaskId(), request.getAssignee());
        Execution currentExecution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(currentExecution.getProcessInstanceId()).singleResult();
        List<Task> allTasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().list();
        // 获取当前任务的节点id
        String taskDefinitionKey = task.getTaskDefinitionKey();
        //多实例任务
        List<Task> countersignTasks = allTasks.stream()
                .filter(t -> t.getTaskDefinitionKey().equals(taskDefinitionKey) && !t.getId().equals(task.getId()))
                .collect(Collectors.toList());

        if (ObjectUtil.isNotEmpty(request.getVariables())) {
            request.getVariables().put("previousAssignee",request.getAssignee());
            //false 代表流程变量  非本地变量
            taskService.complete(request.getTaskId(), request.getVariables(), false);
        } else {
            Map<String, Object> variables = Maps.newHashMap();
            variables.put("previousAssignee",request.getAssignee());
            taskService.complete(request.getTaskId(),variables,false);
        }
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        EsbEmployeeDTO employeeDTO = esbEmployeeApi.getByEmpId(historicProcessInstance.getStartUserId());
        //多实例任务oa待办消息变为已办
        if(CollUtil.isNotEmpty(countersignTasks)){
            countersignTasks.forEach(countersignTask->{
                ProcessDoneRequest processDoneRequest = new ProcessDoneRequest();
                String title = historicProcessInstance.getProcessDefinitionName()+"-"+employeeDTO.getEmpName()+"-"+ DateUtil.format(historicProcessInstance.getStartTime(), DatePattern.NORM_DATE_PATTERN)+"("+historicProcessInstance.getBusinessKey()+")";
                processDoneRequest.setWorkflowName(historicProcessInstance.getProcessDefinitionName()).setTitle(title).setReceiverCode(countersignTask.getAssignee()).setBizId(countersignTask.getId());
                MqMessageBO nextMqMessageBO = new MqMessageBO(Constants.TOPIC_OA_TODO_DONE, Constants.TAG_OA_TODO_DONE, JSON.toJSONString(processDoneRequest) );
                nextMqMessageBO = mqMessageSendApi.prepare(nextMqMessageBO);
                mqMessageSendApi.send(nextMqMessageBO);
            });
        }


        //当前任务oa待办消息变为已办
        ProcessDoneRequest processDoneRequest = new ProcessDoneRequest();

        FormDTO formDTO = formApi.getByFlowId(historicProcessInstance.getId());
        String title = historicProcessInstance.getProcessDefinitionName()+"-"+employeeDTO.getEmpName()+"-"+ DateUtil.format(historicProcessInstance.getStartTime(), DatePattern.NORM_DATE_PATTERN)+"("+historicProcessInstance.getBusinessKey()+")";
        processDoneRequest.setWorkflowName(historicProcessInstance.getProcessDefinitionName()).setTitle(title).setReceiverCode(task.getAssignee()).setBizId(task.getId());
        MqMessageBO nextMqMessageBO = new MqMessageBO(Constants.TOPIC_OA_TODO_DONE, Constants.TAG_OA_TODO_DONE, JSON.toJSONString(processDoneRequest) );
        nextMqMessageBO = mqMessageSendApi.prepare(nextMqMessageBO);
        mqMessageSendApi.send(nextMqMessageBO);
        //发送下一节点处理人代办消息
        List<Task> list = taskService.createTaskQuery().active().processInstanceId(historicProcessInstance.getId()).list();
        list.forEach(todoTask -> {
            wfProcessService.sendOaTodoMsg(historicProcessInstance.getId(),todoTask.getId(),historicProcessInstance.getStartTime(),historicProcessInstance.getStartUserId(),todoTask.getAssignee(),historicProcessInstance.getProcessDefinitionName(),employeeDTO.getEmpName(),historicProcessInstance.getBusinessKey(),formDTO.getType(),formDTO.getId(),false,todoTask.getName());
        });

    }

    @Override
    @GlobalTransactional
    public void returnTask(CompleteWfTaskRequest request) {
        Task task = taskService.createTaskQuery().taskId(request.getTaskId()).singleResult();
        if (Objects.isNull(task)) {
            throw new BusinessException(ResultCode.FAILED, "任务不存在");
        }
        if (task.isSuspended()) {
            throw new BusinessException(ResultCode.FAILED, "任务处于挂起状态");
        }
        List<WorkFlowNodeDTO> nodeDTOList = findReturnTaskList(task.getId());
        if (CollUtil.isEmpty(nodeDTOList)) {
            log.error("回退失败，不存在可回退节点request={}", request.toString());
            throw new BusinessException(ResultCode.FAILED, "驳回失败");
        }
        Execution currentExecution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(currentExecution.getProcessInstanceId()).singleResult();
        List<Task> allTasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().list();


        //处理提交人节点 不在自动跳过，以便修改完业务数据重新提交 审批该任务
        runtimeService.setVariable(task.getProcessInstanceId(), FlowConstant.SKIP, false);
        runtimeService.setVariable(task.getProcessInstanceId(),"previousAssignee", "");
        if(StrUtil.isEmpty(request.getNodeId())){
            runtimeService.updateBusinessStatus(task.getProcessInstanceId(), FormStatusEnum.REJECT.getName());
        }
        taskService.addComment(task.getId(), task.getProcessInstanceId(), FlowCommentEnum.REBACK.getType(), request.getComment());
        if(StrUtil.isNotEmpty(request.getNodeId())){
            runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId()).moveActivityIdTo(task.getTaskDefinitionKey(), request.getNodeId()).changeState();

        }else{
            runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId()).moveActivityIdTo(task.getTaskDefinitionKey(), nodeDTOList.get(0).getNodeId()).changeState();
        }

        // 获取当前任务的节点id
        String taskDefinitionKey = task.getTaskDefinitionKey();
        //多实例任务
        List<Task> countersignTasks = allTasks.stream()
                .filter(t -> t.getTaskDefinitionKey().equals(taskDefinitionKey) && !t.getId().equals(task.getId()))
                .collect(Collectors.toList());
        //驳回到发起人才改变表单状态
        FormDTO formDTO = formApi.getById(request.getGbId());
        if(StrUtil.isEmpty(request.getNodeId())){

            // 修改表单状态
            UpdateGBFormInfoRequest updateRequest = new UpdateGBFormInfoRequest();
            updateRequest.setId(request.getGbId()).setNewStatus(FormStatusEnum.REJECT)
                    .setOriginalStatus(FormStatusEnum.AUDITING).setOpUserId(request.getOpUserId());
            if(formDTO.getType().equals(FormTypeEnum.GB_CANCEL.getCode()) || formDTO.getType().equals(FormTypeEnum.GB_SUBMIT.getCode())){
                gbFormApi.updateStatusById(updateRequest);
            }else{
                // 发送状态变更mq
                updateRequest.setFormType(formDTO.getType());
                MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_CHANGE_FORM_REJECT,Constants.TAG_CHANGE_FORM_REJECT, JSON.toJSONString(updateRequest) );
                mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
                mqMessageSendApi.send(mqMessageBO);
            }

        }



        AddWfActHistoryRequest addWfActHistoryRequest = new AddWfActHistoryRequest();
        addWfActHistoryRequest.setFormId(request.getGbId()).setFromEmpId(request.getAssignee()).setType(WfActTypeEnum.REJECT.getCode()).setText(request.getComment());
        wfActHistoryService.addActHistory(addWfActHistoryRequest);

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        EsbEmployeeDTO employeeDTO = esbEmployeeApi.getByEmpId(historicProcessInstance.getStartUserId());
        //多实例任务oa待办消息变为已办
        if(CollUtil.isNotEmpty(countersignTasks)){
            countersignTasks.forEach(countersignTask->{
                ProcessDoneRequest processDoneRequest = new ProcessDoneRequest();
                String title = historicProcessInstance.getProcessDefinitionName()+"-"+employeeDTO.getEmpName()+"-"+ DateUtil.format(historicProcessInstance.getStartTime(), DatePattern.NORM_DATE_PATTERN)+"("+historicProcessInstance.getBusinessKey()+")";
                processDoneRequest.setWorkflowName(historicProcessInstance.getProcessDefinitionName()).setTitle(title).setReceiverCode(countersignTask.getAssignee()).setBizId(countersignTask.getId());
                MqMessageBO nextMqMessageBO = new MqMessageBO(Constants.TOPIC_OA_TODO_DONE, Constants.TAG_OA_TODO_DONE, JSON.toJSONString(processDoneRequest) );
                nextMqMessageBO = mqMessageSendApi.prepare(nextMqMessageBO);
                mqMessageSendApi.send(nextMqMessageBO);
            });
        }
        //消息变为已办
        ProcessDoneRequest processDoneRequest = new ProcessDoneRequest();
        String title = historicProcessInstance.getProcessDefinitionName()+"-"+employeeDTO.getEmpName()+"-"+ DateUtil.format(historicProcessInstance.getStartTime(), DatePattern.NORM_DATE_PATTERN)+"("+historicProcessInstance.getBusinessKey()+")";
        processDoneRequest.setWorkflowName(historicProcessInstance.getProcessDefinitionName()).setTitle(title).setReceiverCode(task.getAssignee()).setBizId(task.getId());
        MqMessageBO nextMqMessageBO = new MqMessageBO(Constants.TOPIC_OA_TODO_DONE, Constants.TAG_OA_TODO_DONE, JSON.toJSONString(processDoneRequest) );
        nextMqMessageBO = mqMessageSendApi.prepare(nextMqMessageBO);
        mqMessageSendApi.send(nextMqMessageBO);
        //驳回后给发起人发送待办消息
        List<Task> list = taskService.createTaskQuery().active().processInstanceId(historicProcessInstance.getId()).list();
        if(StrUtil.isEmpty(request.getNodeId())) {
            this.sendSms(null, FormTypeEnum.getByCode(formDTO.getType()), historicProcessInstance.getBusinessKey(), historicProcessInstance.getStartUserId());
        }else{
            this.sendSms(list, FormTypeEnum.getByCode(formDTO.getType()), historicProcessInstance.getBusinessKey(), null);
        }

        list.forEach(todoTask -> {
            if(StrUtil.isEmpty(request.getNodeId())){
                //驳回到发起人给发人发pa待办
                wfProcessService.sendOaTodoMsg(historicProcessInstance.getId(),todoTask.getId(),historicProcessInstance.getStartTime(),historicProcessInstance.getStartUserId(),historicProcessInstance.getStartUserId(),historicProcessInstance.getProcessDefinitionName(),employeeDTO.getEmpName(),historicProcessInstance.getBusinessKey(),formDTO.getType(),formDTO.getId(),false,todoTask.getName());
            }else{
                //驳回到非发起人发送oa待办
                wfProcessService.sendOaTodoMsg(historicProcessInstance.getId(),todoTask.getId(),historicProcessInstance.getStartTime(),historicProcessInstance.getStartUserId(),todoTask.getAssignee(),historicProcessInstance.getProcessDefinitionName(),employeeDTO.getEmpName(),historicProcessInstance.getBusinessKey(),formDTO.getType(),formDTO.getId(),false,todoTask.getName());
            }
        });
    }

    private void sendSms(List<Task> list,FormTypeEnum formTypeEnum,String code,String startUser){
        //只有团购才发短信
        if(!formTypeEnum.equals(FormTypeEnum.GB_SUBMIT) && !formTypeEnum.equals(FormTypeEnum.GB_CANCEL) && !formTypeEnum.equals(FormTypeEnum.GROUP_BUY_COST)){
            return;
        }
        String content = StrFormatter.format(FlowConstant.REJECT_SMS_TEMPLATE,formTypeEnum.getName(),code);
        if(StrUtil.isNotEmpty(startUser)){
            EsbEmployeeDTO employeeDTO = esbEmployeeApi.getByEmpId(startUser);
            smsApi.send(employeeDTO.getMobilePhone(),content, SmsTypeEnum.GROUP_BUY_REJECT);
        }else{
            List<String> collect = list.stream().map(Task::getAssignee).collect(Collectors.toList());
            List<EsbEmployeeDTO> esbEmployeeDTOS = esbEmployeeApi.listByEmpIds(collect);
            List<String> mobileList = esbEmployeeDTOS.stream().map(EsbEmployeeDTO::getMobilePhone).collect(Collectors.toList());
            mobileList.forEach(mobile->{
                smsApi.send(mobile,content, SmsTypeEnum.GROUP_BUY_REJECT);
            });
        }
    }

    @Override
    public List<WorkFlowNodeDTO> findReturnTaskList(String taskId) {
        // 初始化返回结果列表
        List<WorkFlowNodeDTO> result = new ArrayList<>(16);
        if (StringUtils.isBlank(taskId)) {
            return result;
        }
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return result;
        }
        // 任务定义key 等于 当前任务节点id
        String taskDefinitionKey = task.getTaskDefinitionKey();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        Process mainProcess = bpmnModel.getMainProcess();
        // 当前节点
        FlowNode currentFlowElement = (FlowNode) mainProcess.getFlowElement(taskDefinitionKey, true);
        // 查询历史节点实例
        List<HistoricActivityInstance> activityInstanceList = historyService.createHistoricActivityInstanceQuery().processInstanceId(task.getProcessInstanceId()).finished().orderByHistoricActivityInstanceEndTime().asc().list();
        List<String> activityIdList = activityInstanceList.stream().filter(activityInstance -> BpmnXMLConstants.ELEMENT_TASK_USER.equals(activityInstance.getActivityType())).map(HistoricActivityInstance::getActivityId).filter(activityId -> !taskDefinitionKey.equals(activityId)).distinct().collect(Collectors.toList());
        for (String activityId : activityIdList) {
            // 回退到主流程的节点
            FlowNode toBackFlowElement = (FlowNode) mainProcess.getFlowElement(activityId, true);
            // 判断 【工具类判断是否可以从源节点 到 目标节点】
            Set<String> set = new HashSet<>();
            if (toBackFlowElement != null && ExecutionGraphUtil.isReachable(mainProcess, toBackFlowElement, currentFlowElement, set)) {
                WorkFlowNodeDTO workFlowNodeDTO = new WorkFlowNodeDTO();
                workFlowNodeDTO.setNodeId(activityId);
                workFlowNodeDTO.setNodeName(toBackFlowElement.getName());
                result.add(workFlowNodeDTO);
            }
        }
        return result;
    }

    @Override
    @GlobalTransactional
    public void reSubmit(String businessKey, Map<String, Object> variables) {
        //使用businessKey查看任务
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
        if (Objects.isNull(task)) {
            log.error("任务不存在businessKey={}", businessKey);
            throw new BusinessException(ResultCode.FAILED, "任务不存在");
        }
        HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery().processInstanceId(task.getProcessInstanceId()).variableName(FlowConstant.GB_ID).singleResult();
        UpdateGBFormInfoRequest updateRequest = new UpdateGBFormInfoRequest();
        updateRequest.setId((long)historicVariableInstance.getValue()).setNewStatus(FormStatusEnum.AUDITING)
                .setOriginalStatus(FormStatusEnum.REJECT);
        FormDTO formDTO = formApi.getById((long)historicVariableInstance.getValue());
        if(formDTO.getType().equals(FormTypeEnum.GB_CANCEL.getCode()) || formDTO.getType().equals(FormTypeEnum.GB_SUBMIT.getCode())){
            gbFormApi.updateStatusById(updateRequest);
        }else{
            // 发送状态变更mq
            updateRequest.setFormType(formDTO.getType());
            MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_CHANGE_FORM_RESUBMIT,Constants.TAG_CHANGE_FORM_RESUBMIT, JSON.toJSONString(updateRequest) );
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
            mqMessageSendApi.send(mqMessageBO);
        }
        //taskService.addComment(task.getId(), task.getProcessInstanceId(), FlowCommentEnum.NORMAL.getType(), "");
        if (ObjectUtil.isNotEmpty(variables)) {
            variables.put("previousAssignee","");
            //false 代表流程变量  非本地变量
            taskService.complete(task.getId(),variables, false);
        } else {
            taskService.complete(task.getId());
        }

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        EsbEmployeeDTO employeeDTO = esbEmployeeApi.getByEmpId(historicProcessInstance.getStartUserId());
        AddWfActHistoryRequest addWfActHistoryRequest = new AddWfActHistoryRequest();
        addWfActHistoryRequest.setFormId(Convert.toLong(historicVariableInstance.getValue(), 0L)).setFromEmpId(historicProcessInstance.getStartUserId()).setType(WfActTypeEnum.SUBMIT.getCode());
        wfActHistoryService.addActHistory(addWfActHistoryRequest);
        //消息变为已办
        ProcessDoneRequest processDoneRequest = new ProcessDoneRequest();
        String title = historicProcessInstance.getProcessDefinitionName()+"-"+employeeDTO.getEmpName()+"-"+ DateUtil.format(historicProcessInstance.getStartTime(), DatePattern.NORM_DATE_PATTERN)+"("+historicProcessInstance.getBusinessKey()+")";
        processDoneRequest.setWorkflowName(historicProcessInstance.getProcessDefinitionName()).setTitle(title).setReceiverCode(historicProcessInstance.getStartUserId()).setBizId(task.getId());
        MqMessageBO nextMqMessageBO = new MqMessageBO(Constants.TOPIC_OA_TODO_DONE, Constants.TAG_OA_TODO_DONE, JSON.toJSONString(processDoneRequest) );
        nextMqMessageBO = mqMessageSendApi.prepare(nextMqMessageBO);
        mqMessageSendApi.send(nextMqMessageBO);
        //发送下一节点处理人代办消息
        List<Task> list = taskService.createTaskQuery().active().processInstanceId(historicProcessInstance.getId()).list();
        list.forEach(todoTask -> {
            wfProcessService.sendOaTodoMsg(historicProcessInstance.getId(),todoTask.getId(),historicProcessInstance.getStartTime(),historicProcessInstance.getStartUserId(),todoTask.getAssignee(),historicProcessInstance.getProcessDefinitionName(),employeeDTO.getEmpName(),historicProcessInstance.getBusinessKey(),formDTO.getType(),formDTO.getId(),false,todoTask.getName());
        });

    }


    @Override
    @GlobalTransactional
    public void commonReSubmit(CommonResubmitRequest commonResubmitRequest) {
        FormDTO formDTO = formApi.getById(commonResubmitRequest.getId());
        String businessKey = formDTO.getCode();
        Map<String, Object>  variables = commonResubmitRequest.getVariables();
        //使用businessKey查看任务
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
        if (Objects.isNull(task)) {
            log.error("任务不存在businessKey={}", businessKey);
            throw new BusinessException(ResultCode.FAILED, "任务不存在");
        }
        UpdateGBFormInfoRequest updateRequest = new UpdateGBFormInfoRequest();
        updateRequest.setId(commonResubmitRequest.getId()).setNewStatus(FormStatusEnum.AUDITING)
                .setOriginalStatus(FormStatusEnum.REJECT);
        if (ObjectUtil.isNotEmpty(variables)) {
            variables.put("previousAssignee","");
            //false 代表流程变量  非本地变量
            taskService.complete(task.getId(),variables, false);
        } else {
            taskService.complete(task.getId());
        }
        // 发送状态变更mq
        updateRequest.setFormType(formDTO.getType());
        MqMessageBO updateMq = new MqMessageBO(Constants.TOPIC_CHANGE_FORM_RESUBMIT,Constants.TAG_CHANGE_FORM_RESUBMIT, JSON.toJSONString(updateRequest) );
        updateMq = mqMessageSendApi.prepare(updateMq);
        mqMessageSendApi.send(updateMq);

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        EsbEmployeeDTO employeeDTO = esbEmployeeApi.getByEmpId(historicProcessInstance.getStartUserId());
        AddWfActHistoryRequest addWfActHistoryRequest = new AddWfActHistoryRequest();
        addWfActHistoryRequest.setFormId(commonResubmitRequest.getId()).setFromEmpId(historicProcessInstance.getStartUserId()).setType(WfActTypeEnum.SUBMIT.getCode());
        wfActHistoryService.addActHistory(addWfActHistoryRequest);
        //消息变为已办
        ProcessDoneRequest processDoneRequest = new ProcessDoneRequest();
        String title = historicProcessInstance.getProcessDefinitionName()+"-"+employeeDTO.getEmpName()+"-"+ DateUtil.format(historicProcessInstance.getStartTime(), DatePattern.NORM_DATE_PATTERN)+"("+historicProcessInstance.getBusinessKey()+")";
        processDoneRequest.setWorkflowName(historicProcessInstance.getProcessDefinitionName()).setTitle(title).setReceiverCode(historicProcessInstance.getStartUserId()).setBizId(task.getId());
        MqMessageBO nextMqMessageBO = new MqMessageBO(Constants.TOPIC_OA_TODO_DONE, Constants.TAG_OA_TODO_DONE, JSON.toJSONString(processDoneRequest) );
        nextMqMessageBO = mqMessageSendApi.prepare(nextMqMessageBO);
        mqMessageSendApi.send(nextMqMessageBO);
        //发送下一节点处理人代办消息
        List<Task> list = taskService.createTaskQuery().active().processInstanceId(historicProcessInstance.getId()).list();
        list.forEach(todoTask -> {
            wfProcessService.sendOaTodoMsg(historicProcessInstance.getId(),todoTask.getId(),historicProcessInstance.getStartTime(),historicProcessInstance.getStartUserId(),todoTask.getAssignee(),historicProcessInstance.getProcessDefinitionName(),employeeDTO.getEmpName(),historicProcessInstance.getBusinessKey(),formDTO.getType(),formDTO.getId(),false,todoTask.getName());
        });
    }

    @Override
    public String genProcessDiagram(String processId) {
        InputStream inputStream = flowUtils.getResourceDiagramInputStream(processId);
/*        try {
            // 先将图片保存
            InputStreamReader inputStreamReader
                    = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            FileUtils.copyInputStreamToFile(inputStream, new File("D:\\", "1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        byte[] bytes = this.inputStreamToByteArray(inputStream);
        BASE64Encoder encoder = new BASE64Encoder();
        String png_base64 = encoder.encodeBuffer(bytes).trim();
        //删除 \r\n
        png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");

        return png_base64;
    }

    /**
     * 流转字节
     *
     * @param inputStream
     * @return
     */
    private byte[] inputStreamToByteArray(InputStream inputStream) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int num;
            while ((num = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, num);
            }
            byteArrayOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }

    @Override
    public WfTaskDTO getById(GetTaskDetailRequest request) {
        WfTaskDTO taskDTO = new WfTaskDTO();
        HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().taskId(request.getTaskId()).singleResult();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(taskInstance.getProcessInstanceId()).singleResult();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult();
        taskDTO.setCategory(processDefinition.getCategory()).setTaskName(taskInstance.getName());
        HistoricVariableInstance var = historyService.createHistoricVariableInstanceQuery().processInstanceId(taskInstance.getProcessInstanceId()).variableName(FlowConstant.GB_ID).singleResult();
        if(Objects.nonNull(var)){
            taskDTO.setGbId((long)var.getValue());
        }
        if(taskInstance.getName().equals("市场运营部省区经理核实审批") && historicProcessInstance.getProcessDefinitionName().equals("团购提报") && request.getCurrentUserCode().equals(taskInstance.getAssignee())){
            taskDTO.setNeedCheck(true);
        }else{
            taskDTO.setNeedCheck(false);
        }
        if(Objects.nonNull(taskInstance.getEndTime()) || !request.getCurrentUserCode().equals(taskInstance.getAssignee())){
            taskDTO.setIsFinished(true);
        }else{
            taskDTO.setIsFinished(false);
        }

        return taskDTO;
    }

    @Override
    public WfTaskDTO getByFormId(Long formId,String userCode,Long forwardHistoryId) {
        FormDTO formDTO = formApi.getById(formId);
        Task task = null;//taskService.createTaskQuery().active().taskAssignee(userCode).processInstanceId(formDTO.getFlowId()).singleResult();
        if(Objects.nonNull(forwardHistoryId) && forwardHistoryId>0){
            task = taskService.createTaskQuery().active().taskAssignee(userCode).processInstanceId(formDTO.getFlowId()).executionId(forwardHistoryId.toString()+"-"+userCode).singleResult();
        }else{
            List<Task>  tasks = taskService.createTaskQuery().active().taskAssignee(userCode).processInstanceId(formDTO.getFlowId()).list();
            task = tasks.stream().filter(t-> StrUtil.isNotEmpty(t.getTaskDefinitionKey())).findAny().orElse(null);
        }
        if(Objects.isNull(task)){
            return null;
        }
        WfTaskDTO wfTaskDTO = new WfTaskDTO();
        wfTaskDTO.setTaskId(task.getId()).setExecutionId(task.getExecutionId()).setTaskName(task.getName());
        return wfTaskDTO;
    }

    @Override
    public void forward(AddForwardHistoryRequest request) {
        wfActHistoryService.addForwardHistory(request);
    }

    @Override
    public void comment(AddCommentHistoryRequest request) {
        wfActHistoryService.addCommentHistory(request);
    }

    @Override
    public List<WfActHistoryDTO> listHistoryByForm(Long formId) {
        return PojoUtils.map(wfActHistoryService.listByFormId(formId), WfActHistoryDTO.class);
    }

    @Override
    public CommentDTO queryComment(String code) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(code).singleResult();
        if(Objects.isNull(historicProcessInstance)){
            return new CommentDTO();
        }
        List<HistoricTaskInstance> taskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(historicProcessInstance.getId()).finished().orderByHistoricTaskInstanceEndTime().desc().list();
        if(CollUtil.isEmpty(taskInstances)){
            return new CommentDTO();
        }
        HistoricTaskInstance taskInstance = taskInstances.stream().filter(t -> t.getName().equals(FlowConstant.FINANCE_TASK_NAME)).findFirst().orElse(null);
        if(Objects.isNull(taskInstance)){
            return new CommentDTO();
        }
        List<Comment> commentList = taskService.getTaskComments(taskInstance.getId(),FlowCommentEnum.PASS.getType());
        if(CollUtil.isEmpty(commentList)){
            return new CommentDTO();
        }
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setFullMessage(commentList.get(0).getFullMessage());
        return commentDTO;
    }
}
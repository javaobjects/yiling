package com.yiling.workflow.workflow.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.Constants;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.gb.api.GbFormApi;
import com.yiling.sjms.gb.dto.request.UpdateGBFormInfoRequest;
import com.yiling.sjms.oa.todo.dto.request.ReceiveTodoRequest;
import com.yiling.sjms.util.OaTodoUtils;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.workflow.factory.FlowServiceFactory;
import com.yiling.workflow.workflow.constant.FlowConstant;
import com.yiling.workflow.workflow.dto.ProcessDetailDTO;
import com.yiling.workflow.workflow.dto.ProcessInstanceDTO;
import com.yiling.workflow.workflow.dto.WfTaskDTO;
import com.yiling.workflow.workflow.dto.request.AddWfActHistoryRequest;
import com.yiling.workflow.workflow.dto.request.GetProcessDetailRequest;
import com.yiling.workflow.workflow.dto.request.QueryFinishedProcessPageRequest;
import com.yiling.workflow.workflow.dto.request.QueryTodoProcessPageRequest;
import com.yiling.workflow.workflow.dto.request.StartProcessRequest;
import com.yiling.workflow.workflow.enums.FlowCommentEnum;
import com.yiling.workflow.workflow.enums.WfActTypeEnum;
import com.yiling.workflow.workflow.service.WfActHistoryService;
import com.yiling.workflow.workflow.service.WfProcessService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * 流程
 * @author: gxl
 * @date: 2022/11/28
 */
@RefreshScope
@Service
@Slf4j
public class WfProcessServiceImpl extends FlowServiceFactory implements WfProcessService {

    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    private GbFormApi gbFormApi;

    @DubboReference
    private FormApi formApi;

    @Autowired
    private WfActHistoryService wfActHistoryService;

    @Autowired
    private RedisService redisService;

    @Value("#{${common.gb.process-parameter}}")
    Map<String, String> gbProcess ;

    @Value("${oa.sso.url:123}")
    private String oaSsoUrl;
    @Value("${oa.todo.pcUrl:123}")
    private String oaTodoPcUrl;
    @Value("${oa.todo.appUrl:123}")
    private String oaTodoAppUrl;


    @DubboReference
    private MqMessageSendApi mqMessageSendApi;

    @Override
    public Page<WfTaskDTO> queryPageTodoProcessList(QueryTodoProcessPageRequest request) {
        Page<WfTaskDTO> page = request.getPage();
        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .taskAssignee(request.getCurrentUserCode())
                .orderByTaskCreateTime().desc();
        if(StrUtil.isNotEmpty(request.getBusinessKey())){
            taskQuery.processInstanceBusinessKey(request.getBusinessKey());
        }
        if(StrUtil.isNotEmpty(request.getCategory())){
            taskQuery.processCategoryIn(CollUtil.newHashSet(request.getCategory()));
        }
        page.setTotal(taskQuery.count());
        int offset = request.getSize() * (request.getCurrent() - 1);
        List<Task> taskList = taskQuery.listPage(offset, request.getSize());
        if(CollUtil.isEmpty(taskList)){
            return page;
        }
        Set<String> processInsIds = taskList.stream().map(Task::getProcessInstanceId).collect(Collectors.toSet());
        List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery().processInstanceIds(processInsIds).list();
        Map<String, HistoricProcessInstance> processInstanceMap = historicProcessInstanceList.stream().collect(Collectors.toMap(HistoricProcessInstance::getId, Function.identity()));
        //List<String> startUserIds = historicProcessInstanceList.stream().map(HistoricProcessInstance::getStartUserId).distinct().collect(Collectors.toList());
        //List<EsbEmployeeDTO> esbEmployeeDTOS = esbEmployeeApi.listByEmpIds(startUserIds);
       // Map<String, EsbEmployeeDTO> esbEmployeeDTOMap = esbEmployeeDTOS.stream().collect(Collectors.toMap(EsbEmployeeDTO::getEmpId, Function.identity()));
        Set<String> processDefinitionIdSet = historicProcessInstanceList.stream().map(HistoricProcessInstance::getProcessDefinitionId).collect(Collectors.toSet());
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().processDefinitionIds(processDefinitionIdSet).list();
        Map<String, ProcessDefinition> processDefMap = processDefinitionList.stream().collect(Collectors.toMap(ProcessDefinition::getId, Function.identity()));

        List<WfTaskDTO> flowList = new ArrayList<>();
        for (Task task : taskList) {
            WfTaskDTO flowTask = new WfTaskDTO();
            String redisKey = RedisKey.generate("workflow", task.getId());
            Object autoApproveKey = redisService.get(redisKey);
            if(Objects.nonNull(autoApproveKey)){
                log.debug("自动审批数据不显示在列表中taskId={}",task.getId());
                page.setTotal(page.getTotal()-1);
                continue;
            }
            // 当前流程信息
            flowTask.setTaskId(task.getId());
            flowTask.setTaskDefKey(task.getTaskDefinitionKey());
            flowTask.setProcDefId(task.getProcessDefinitionId());
            flowTask.setTaskName(task.getName());
            flowTask.setProcInsId(task.getProcessInstanceId());
            HistoricProcessInstance historicProcessInstance = processInstanceMap.get(task.getProcessInstanceId());
            if(task.getName().equals("市场运营部省区经理") && historicProcessInstance.getProcessDefinitionName().equals("团购提报")){
                flowTask.setNeedCheck(true);
            }else{
                flowTask.setNeedCheck(false);
            }
            if(task.getName().equals(FlowConstant.FORWARD_NODE)){
                flowTask.setForwardHistoryId(Long.parseLong(task.getExecutionId().split("-")[0]));
            }
            ProcessDefinition processDefinition = processDefMap.get(historicProcessInstance.getProcessDefinitionId());
            flowTask.setCategory(processDefinition.getCategory());
            flowTask.setProcDefName(historicProcessInstance.getProcessDefinitionName());

            flowTask.setBusinessKey(historicProcessInstance.getBusinessKey());
            flowTask.setAssigneeId(task.getAssignee());
         /*   HistoricVariableInstance var = historyService.createHistoricVariableInstanceQuery().processInstanceId(task.getProcessInstanceId()).variableName(FlowConstant.GB_ID).singleResult();
            Long formId = null;
            if(Objects.nonNull(var)){
                flowTask.setGbId((long)var.getValue());
                formId = flowTask.getGbId();
            }else{
                HistoricVariableInstance formIdVar = historyService.createHistoricVariableInstanceQuery().processInstanceId(task.getProcessInstanceId()).variableName(FlowConstant.FORM_ID).singleResult();
                if(Objects.nonNull(formIdVar)){
                    flowTask.setFormId((long)formIdVar.getValue());
                    formId = flowTask.getFormId();
                }
            }*/

            FormDTO formDTO = formApi.getByFlowId(historicProcessInstance.getId());
            flowTask.setFormId(formDTO.getId());
            flowTask.setStartUserId(formDTO.getEmpId());
            flowTask.setStartUserName(formDTO.getEmpName());
            flowTask.setStartDeptName(formDTO.getBizDept());
            flowTask.setFormType(formDTO.getType());
            flowTask.setGbNo(historicProcessInstance.getBusinessKey());
            flowTask.setCreateTime(formDTO.getSubmitTime());
            flowList.add(flowTask);
        }
        page.setRecords(flowList);
        return page;
    }

    @Override
    public Page<WfTaskDTO> queryFinishedProcessList(QueryFinishedProcessPageRequest request) {
        Page<WfTaskDTO> page = request.getPage();
        HistoricTaskInstanceQuery taskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .finished()
                .taskAssignee(request.getCurrentUserCode())
                .orderByHistoricTaskInstanceEndTime()
                .desc();
        if(StrUtil.isNotEmpty(request.getBusinessKey())){
            taskInstanceQuery.processInstanceBusinessKey(request.getBusinessKey());
        }
        if(StrUtil.isNotEmpty(request.getCategory())){
            taskInstanceQuery.processCategoryIn(CollUtil.newHashSet(request.getCategory()));
        }
        int offset = request.getSize() * (request.getCurrent() - 1);
        page.setTotal(taskInstanceQuery.count());
        List<HistoricTaskInstance> historicTaskInstanceList = taskInstanceQuery.listPage(offset, request.getSize());
        if(CollUtil.isEmpty(historicTaskInstanceList)){
            return page;
        }
        Set<String> processInsIds = historicTaskInstanceList.stream().map(HistoricTaskInstance::getProcessInstanceId).collect(Collectors.toSet());
        List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery().processInstanceIds(processInsIds).list();
        Map<String, HistoricProcessInstance> processInstanceMap = historicProcessInstanceList.stream().collect(Collectors.toMap(HistoricProcessInstance::getId, Function.identity()));
        Set<String> processDefinitionIdSet = historicProcessInstanceList.stream().map(HistoricProcessInstance::getProcessDefinitionId).collect(Collectors.toSet());
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().processDefinitionIds(processDefinitionIdSet).list();
        Map<String, ProcessDefinition> processDefMap = processDefinitionList.stream().collect(Collectors.toMap(ProcessDefinition::getId, Function.identity()));

        List<WfTaskDTO> flowList = new ArrayList<>();
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {
            WfTaskDTO flowTask = new WfTaskDTO();
            flowTask.setProcDefId(historicTaskInstance.getProcessDefinitionId());
            flowTask.setTaskDefKey(historicTaskInstance.getTaskDefinitionKey());
            flowTask.setTaskName(historicTaskInstance.getName());
            HistoricProcessInstance processInstance = processInstanceMap.get(historicTaskInstance.getProcessInstanceId());

            flowTask.setTaskId(historicTaskInstance.getId()).setProcInsId(historicTaskInstance.getProcessInstanceId()).setProcDefName(processInstance.getProcessDefinitionName());

            flowTask.setBusinessKey(processInstance.getBusinessKey());
            ProcessDefinition processDefinition = processDefMap.get(processInstance.getProcessDefinitionId());
            flowTask.setCategory(processDefinition.getCategory());
            flowTask.setAssigneeId(historicTaskInstance.getAssignee());
            FormDTO formDTO = formApi.getByFlowId(processInstance.getId());
            if(Objects.isNull(formDTO)){
                log.debug("form信息不存在processInstanceId={}",processInstance.getId());
                continue;
            }
            flowTask.setFormId(formDTO.getId());
            flowTask.setStartUserId(formDTO.getEmpId());
            flowTask.setStartUserName(formDTO.getEmpName());
            flowTask.setStartDeptName(formDTO.getBizDept());
            flowTask.setFormType(formDTO.getType());
            flowTask.setGbNo(processInstance.getBusinessKey());
            flowTask.setCreateTime(formDTO.getSubmitTime());
            flowList.add(flowTask);
        }
        page.setRecords(flowList);
        return page;
    }

    @Override
   @GlobalTransactional
    public  ProcessInstanceDTO startProcess(StartProcessRequest request) {
        ProcessInstanceDTO processInstanceDTO = null;
        if (StringUtils.isNotBlank(request.getProcDefId())
                && StringUtils.isNotBlank(request.getBusinessKey())){
            if(Objects.isNull(request.getVariables())){
                request.setVariables(Maps.newHashMap());
            }
            if(Objects.nonNull(request.getId())){
                request.getVariables().put(FlowConstant.FORM_ID,request.getId());
            }
            //需要设置跳过表达式
            request.getVariables().put(FlowConstant.SKIP, true);
            request.getVariables().put(FlowConstant.FLOWABLE_SKIP_EXPRESSION_ENABLED, true);
            ProcessInstance processInstance = runtimeService.createProcessInstanceBuilder()
                    .processDefinitionId(request.getProcDefId())
                    .businessKey(request.getBusinessKey().trim()).businessStatus(FormStatusEnum.AUDITING.getName())
                    .variables(request.getVariables())
                    .start();

            taskService.addComment(null, processInstance.getProcessInstanceId(), FlowCommentEnum.NORMAL.getType(), "发起流程申请");
            AddWfActHistoryRequest addWfActHistoryRequest = new AddWfActHistoryRequest();
            addWfActHistoryRequest.setFormId(request.getId()).setFromEmpId(request.getStartUserId()).setType(WfActTypeEnum.SUBMIT.getCode());
            wfActHistoryService.addActHistory(addWfActHistoryRequest);
            UpdateGBFormInfoRequest updateRequest = new UpdateGBFormInfoRequest();
            updateRequest.setSubmitTime(new Date()).setId(request.getId()).setFlowName(processInstance.getProcessDefinitionName()).setFlowId(processInstance.getId()).setNewStatus(FormStatusEnum.AUDITING)
                    .setOriginalStatus(FormStatusEnum.UNSUBMIT)
                    .setFlowTplId(processInstance.getProcessDefinitionId())
                    .setFlowVersion(processInstance.getProcessDefinitionVersion().toString());
            if(Objects.isNull(request.getFormType()) || request.getFormType().equals(FormTypeEnum.GB_SUBMIT.getCode())){
                //团购暂时不改成mq
                gbFormApi.updateStatusById(updateRequest);
            }else{
                updateRequest.setFormType(request.getFormType());
                // 发送状态变更mq
                MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_CHANGE_FORM_SUBMIT, request.getTag(), JSON.toJSONString(updateRequest) );
                mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
                mqMessageSendApi.send(mqMessageBO);
            }

            processInstanceDTO = new ProcessInstanceDTO();
            processInstanceDTO.setStartUserId(processInstance.getStartUserId()).setProcessDefinitionName(processInstance.getProcessDefinitionName()).setStartTime(processInstance.getStartTime()).setProcessInstanceId(processInstance.getProcessInstanceId());
            //发送我的oa待办
            HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceDTO.getProcessInstanceId()).finished().singleResult();
            this.sendOaTodoMsg(processInstance.getProcessInstanceId(),taskInstance.getId(),processInstance.getStartTime(),request.getStartUserId(),request.getStartUserId(),processInstance.getProcessDefinitionName(),request.getEmpName(),request.getBusinessKey(),request.getFormType(),request.getId(),true,taskInstance.getName());
            //发送下一个节点的待办
            List<Task> list = taskService.createTaskQuery().active().processInstanceId(processInstanceDTO.getProcessInstanceId()).list();
            list.forEach(task -> {
                this.sendOaTodoMsg(processInstance.getProcessInstanceId(),task.getId(),processInstance.getStartTime(),request.getStartUserId(),task.getAssignee(),processInstance.getProcessDefinitionName(),request.getEmpName(),request.getBusinessKey(),request.getFormType(),request.getId(),false,task.getName());
            });
            return processInstanceDTO;
        }
        return processInstanceDTO;
    }

    @Override
    @GlobalTransactional
    public void startCancleProcess(StartProcessRequest request) {
        request.setProcDefId(gbProcess.get("gb_cancle"));
        //需要设置跳过表达式
        request.getVariables().put(FlowConstant.SKIP, true);
        request.getVariables().put(FlowConstant.FLOWABLE_SKIP_EXPRESSION_ENABLED, true);
        ProcessInstance processInstance = runtimeService.createProcessInstanceBuilder()
                .processDefinitionId(request.getProcDefId())
                .businessKey(request.getBusinessKey().trim()).businessStatus(FormStatusEnum.AUDITING.getName())
                .variables(request.getVariables())
                .start();
        taskService.addComment(null, processInstance.getProcessInstanceId(), FlowCommentEnum.NORMAL.getType(), "发起流程申请");
        UpdateGBFormInfoRequest updateRequest = new UpdateGBFormInfoRequest();
        long gbId = (long) request.getVariables().get(FlowConstant.GB_ID);
        updateRequest.setSubmitTime(new Date()).setId(gbId).setFlowName(processInstance.getProcessDefinitionName()).setFlowId(processInstance.getId()).setNewStatus(FormStatusEnum.AUDITING)
                .setOriginalStatus(FormStatusEnum.UNSUBMIT)
                .setFlowTplId(processInstance.getProcessDefinitionId())
                .setFlowVersion(processInstance.getProcessDefinitionVersion().toString());
        gbFormApi.updateStatusById(updateRequest);
        AddWfActHistoryRequest addWfActHistoryRequest = new AddWfActHistoryRequest();
        addWfActHistoryRequest.setFormId(Convert.toLong(request.getVariables().get(FlowConstant.GB_ID), 0L)).setFromEmpId(request.getStartUserId()).setType(WfActTypeEnum.SUBMIT.getCode());
        wfActHistoryService.addActHistory(addWfActHistoryRequest);
        HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstance.getProcessInstanceId()).finished().singleResult();

        ReceiveTodoRequest receiveTodoRequest = new ReceiveTodoRequest();
        receiveTodoRequest.setBizId(taskInstance.getId());
        receiveTodoRequest.setCreaterCode(processInstance.getStartUserId());
        receiveTodoRequest.setCreateTime(taskInstance.getCreateTime());
        String appUrl = OaTodoUtils.genAppUrl(oaSsoUrl, oaTodoAppUrl,FormTypeEnum.GB_CANCEL, gbId, 0L);
        receiveTodoRequest.setAppUrl(appUrl);
        String pcUrl = OaTodoUtils.genPcUrl(oaSsoUrl, oaTodoPcUrl,FormTypeEnum.GB_CANCEL, gbId, 0L);
        receiveTodoRequest.setPcUrl(pcUrl);
        receiveTodoRequest.setReceiverCode(processInstance.getStartUserId());
        receiveTodoRequest.setReceiveTime(new Date());
        EsbEmployeeDTO employeeDTO = esbEmployeeApi.getByEmpId(processInstance.getStartUserId());
        String title = processInstance.getProcessDefinitionName()+"-"+employeeDTO.getEmpName()+"-"+ DateUtil.format(taskInstance.getCreateTime(), DatePattern.NORM_DATE_PATTERN)+"("+request.getBusinessKey()+")";
        receiveTodoRequest.setTitle(title);
        receiveTodoRequest.setWorkflowName(processInstance.getProcessDefinitionName());
        receiveTodoRequest.setAutoDone(true);
        receiveTodoRequest.setFormCode(request.getBusinessKey());
        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_OA_TODO_RECV, Constants.TAG_OA_TODO_RECV, JSON.toJSONString(receiveTodoRequest) );
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        //发起人我的待办
        mqMessageSendApi.send(mqMessageBO);
        List<Task> list = taskService.createTaskQuery().active().processInstanceId(processInstance.getProcessInstanceId()).list();
        list.forEach(task -> {
            ReceiveTodoRequest recvTodoRequest = new ReceiveTodoRequest();
            recvTodoRequest.setBizId(task.getId());
            recvTodoRequest.setCreaterCode(processInstance.getStartUserId());
            recvTodoRequest.setCreateTime(processInstance.getStartTime());
            recvTodoRequest.setAppUrl(appUrl);
            recvTodoRequest.setPcUrl(pcUrl);
            recvTodoRequest.setReceiverCode(task.getAssignee());
            recvTodoRequest.setReceiveTime(new Date());
            String nextTitle = processInstance.getProcessDefinitionName()+"-"+employeeDTO.getEmpName()+"-"+ DateUtil.format(processInstance.getStartTime(), DatePattern.NORM_DATE_PATTERN)+"("+request.getBusinessKey()+")";
            recvTodoRequest.setTitle(nextTitle);
            recvTodoRequest.setWorkflowName(processInstance.getProcessDefinitionName());
            recvTodoRequest.setFormCode(request.getBusinessKey());
            MqMessageBO nextMqMessageBO = new MqMessageBO(Constants.TOPIC_OA_TODO_RECV, Constants.TAG_OA_TODO_RECV, JSON.toJSONString(recvTodoRequest) );
            nextMqMessageBO = mqMessageSendApi.prepare(nextMqMessageBO);
            //发起人我的待办
            mqMessageSendApi.send(nextMqMessageBO);
        });
    }

    /**
     * 获取多实例任务的其他task
     * @param task
     * @return
     */
    private  List<HistoricTaskInstance> queryMultiTask(HistoricTaskInstance task ,List<HistoricTaskInstance> historicTaskInstanceList){
        // 获取当前任务的节点id
        String taskDefinitionKey = task.getTaskDefinitionKey();
        //多实例任务
        List<HistoricTaskInstance> countersignTasks = historicTaskInstanceList.stream()
                .filter(t -> t.getTaskDefinitionKey().equals(taskDefinitionKey)&&DateUtil.format(t.getStartTime(),DatePattern.NORM_DATETIME_MINUTE_FORMAT).equals(DateUtil.format(task.getStartTime(),DatePattern.NORM_DATETIME_MINUTE_FORMAT))  && !t.getId().equals(task.getId()) && null!=t.getAssignee() &&!t.getAssignee().equals(task.getAssignee()))
                .collect(Collectors.toList());
        if(CollUtil.isEmpty(countersignTasks)){
            return Lists.newArrayList();
        }
        return countersignTasks;
    }

    @Override
    public List<ProcessDetailDTO> getProcessDetail(GetProcessDetailRequest req) {
        //使用businessKey流程实例查询

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(req.getBusinessKey()).singleResult();
        return this.getProcessDetailByInsId(historicProcessInstance.getId());
    }

    @Override
    public List<ProcessDetailDTO> getProcessDetailByInsId(String proInsId) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(proInsId).singleResult();
        if(Objects.isNull(historicProcessInstance)){
           return Lists.newArrayList();
        }
        String procInsId = historicProcessInstance.getId();
        FormDTO formDTO = formApi.getByFlowId(procInsId);
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(procInsId).orderByHistoricTaskInstanceEndTime().asc()
                .list();


        //按结束时间排序，把结束时间空的排到最后面
        List<HistoricTaskInstance> endTimeNullInst = historicTaskInstanceList.stream().filter(historicActivityInstance -> null == historicActivityInstance.getEndTime()).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(endTimeNullInst)){
            historicTaskInstanceList.removeAll(endTimeNullInst);
            historicTaskInstanceList.addAll(endTimeNullInst);
        }
        List<Comment> commentList = taskService.getProcessInstanceComments(procInsId);
        final List<String> hisTaskIds = commentList.stream().map(Comment::getTaskId).collect(Collectors.toList());
        List<ProcessDetailDTO> processDetailDTOS = Lists.newArrayListWithExpectedSize(historicTaskInstanceList.size());
        HashMap<String, String> map = Maps.newHashMap();
        Map<String, HistoricTaskInstance> historicTaskInstanceMap = historicTaskInstanceList.stream().collect(Collectors.toMap(HistoricTaskInstance::getId, Function.identity()));
        historicTaskInstanceList.forEach(activityInstance -> {
            ProcessDetailDTO processDetailDTO = new ProcessDetailDTO();
            if(activityInstance.getName().equals(FlowConstant.FORWARD_NODE)){
                return;
            }
            processDetailDTO.setEndTime(activityInstance.getEndTime());
            List<HistoricTaskInstance> tasks = this.queryMultiTask(activityInstance,historicTaskInstanceList);
            if(map.size()>0 && null!=map.get(activityInstance.getId())){
               return;
            }
            processDetailDTO.setActivityName(activityInstance.getName());
            if (StringUtils.isNotBlank(activityInstance.getAssignee())) {
                EsbEmployeeDTO employeeDTO = esbEmployeeApi.getByEmpId(activityInstance.getAssignee());
                if(Objects.nonNull(employeeDTO)){
                    if(tasks.size()>0){
                        List<EsbEmployeeDTO> esbEmployeeDTOS = esbEmployeeApi.listByEmpIds(tasks.stream().map(HistoricTaskInstance::getAssignee).collect(Collectors.toList()));
                        if(hisTaskIds.contains(activityInstance.getId())){
                            List<String> collect = esbEmployeeDTOS.stream().map(EsbEmployeeDTO::getEmpName).collect(Collectors.toList());
                            processDetailDTO.setAssigneeName("【"+employeeDTO.getEmpName()+"】/"+StringUtils.join(collect,"/"));
                        }else{
                            Map<String, String> stringStringMap = esbEmployeeDTOS.stream().collect(Collectors.toMap(EsbEmployeeDTO::getEmpId, EsbEmployeeDTO::getEmpName));
                            String assignName ="";
                            for (int i = 0; i < tasks.size(); i++) {
                                HistoricTaskInstance taskInstance = historicTaskInstanceMap.get(tasks.get(i).getId());
                                if(hisTaskIds.contains(taskInstance.getId())){
                                    assignName = assignName+"【"+stringStringMap.get(tasks.get(i).getAssignee())+"】";
                                }else{
                                    assignName = assignName+stringStringMap.get(tasks.get(i).getAssignee());
                                }
                                if(i<esbEmployeeDTOS.size()-1){
                                    assignName = assignName+"/";
                                }
                            }
                            processDetailDTO.setAssigneeName(assignName+"/"+employeeDTO.getEmpName());
                        }
                    }else{
                        processDetailDTO.setAssigneeName(employeeDTO.getEmpName());
                    }
                }
            }else {

                processDetailDTO.setAssigneeName(formDTO.getEmpName());
                processDetailDTO.setCommentType(FlowCommentEnum.NORMAL.getType());
                processDetailDTO.setComment("");
            }

            // 获取意见评论内容
            if (CollUtil.isNotEmpty(commentList)) {
                Comment comm = null;
                if(tasks.size()>0 && null!=activityInstance.getEndTime()){
                    comm = commentList.stream().filter(comment -> activityInstance.getId().equals(comment.getTaskId())).findFirst().orElse(null);
                    if(Objects.isNull(comm)){
                        List<String> collect = commentList.stream().map(Comment::getTaskId).collect(Collectors.toList());
                        List<String> collect1 = tasks.stream().map(HistoricTaskInstance::getId).collect(Collectors.toList());
                        List<String> collect2 = CollectionUtil.intersection(collect, collect1).stream().collect(Collectors.toList());
                        if(CollUtil.isNotEmpty(collect2)){
                            comm = commentList.stream().filter(comment -> collect2.get(0).equals(comment.getTaskId())).findFirst().orElse(null);
                        }
                    }
                }else{
                    comm = commentList.stream().filter(comment -> activityInstance.getId().equals(comment.getTaskId())).findFirst().orElse(null);
                }
                if(Objects.nonNull(comm)){
                    processDetailDTO.setComment(comm.getFullMessage()).setCommentType(comm.getType());
                }else if(StrUtil.isEmpty(processDetailDTO.getCommentType())){
                    processDetailDTO.setCommentType(FlowCommentEnum.PROCESSING.getType());
                }
            }
            //多实例
            if(tasks.size()>0){
                tasks.forEach(task -> map.put(task.getId(),activityInstance.getId()));
            }
                processDetailDTOS.add(processDetailDTO);

        });
        return processDetailDTOS;
    }

    @Override
    public void sendOaTodoMsg(String processInstanceId, String taskId, Date createTime,String createrCode, String receiverCode, String processDefinitionName, String empName, String businessKey, Integer formType , Long formId,Boolean autoDone,String taskName) {
        ReceiveTodoRequest receiveTodoRequest = new ReceiveTodoRequest();
        receiveTodoRequest.setBizId(taskId);
        receiveTodoRequest.setCreaterCode(createrCode);
        receiveTodoRequest.setCreateTime(createTime);
        String appUrl= OaTodoUtils.genAppUrl(oaSsoUrl, oaTodoAppUrl,FormTypeEnum.getByCode(formType), formId, 0L);
        receiveTodoRequest.setAppUrl(appUrl);
        String url= OaTodoUtils.genPcUrl(oaSsoUrl, oaTodoPcUrl,FormTypeEnum.getByCode(formType), formId, 0L);
        receiveTodoRequest.setPcUrl(url);
        receiveTodoRequest.setReceiverCode(receiverCode);
        receiveTodoRequest.setReceiveTime(new Date());
        receiveTodoRequest.setAutoDone(autoDone);
        String title = processDefinitionName+"-"+empName+"-"+ DateUtil.format(createTime, DatePattern.NORM_DATE_PATTERN)+"("+businessKey+")";
        receiveTodoRequest.setTitle(title);
        receiveTodoRequest.setWorkflowName(processDefinitionName);
        receiveTodoRequest.setNodeName(taskName);
        receiveTodoRequest.setFormCode(businessKey);
        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_OA_TODO_RECV, Constants.TAG_OA_TODO_RECV, JSON.toJSONString(receiveTodoRequest) );
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        //发起人我的待办
        mqMessageSendApi.send(mqMessageBO);
    }
}
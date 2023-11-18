package com.yiling.workflow.workflow.listener;


import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.util.Constants;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.oa.todo.dto.request.ProcessDoneRequest;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.workflow.factory.FlowServiceFactory;
import com.yiling.workflow.workflow.enums.FlowCommentEnum;
import com.yiling.workflow.workflow.service.WfProcessService;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * 团购相邻节点自动审批
 * @author: gxl
 * @date: 2023/2/14
 */
@Slf4j
@Component(value = "autoCompleteTaskListener")
public class AutoCompleteTaskListener extends FlowServiceFactory  implements TaskListener {

    @DubboReference
    private MqMessageSendApi mqMessageSendApi;

    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    private FormApi formApi;

    @Autowired
    private WfProcessService wfProcessService;

    @Override
    public void notify(DelegateTask delegateTask) {
        String previousAssignee = (String) delegateTask.getVariable("previousAssignee");
        String currentAssignee = delegateTask.getAssignee();
        if (currentAssignee != null && currentAssignee.equals(previousAssignee)) {
            log.debug("相邻节点审批人相同自动审批delegateTask.getId()={}",delegateTask.getId());
            taskService.setAssignee(delegateTask.getId(), delegateTask.getAssignee());
            taskService.addComment(delegateTask.getId(), delegateTask.getProcessInstanceId(), FlowCommentEnum.PASS.getType(),"");
            taskService.complete(delegateTask.getId());
            //消息变为已办
            ProcessDoneRequest processDoneRequest = new ProcessDoneRequest();
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
            FormDTO formDTO = formApi.getByFlowId(historicProcessInstance.getId());
            EsbEmployeeDTO employeeDTO = esbEmployeeApi.getByEmpId(historicProcessInstance.getStartUserId());
            String title = historicProcessInstance.getProcessDefinitionName()+"-"+employeeDTO.getEmpName()+"-"+ DateUtil.format(historicProcessInstance.getStartTime(), DatePattern.NORM_DATE_PATTERN)+"("+historicProcessInstance.getBusinessKey()+")";
            processDoneRequest.setWorkflowName(historicProcessInstance.getProcessDefinitionName()).setTitle(title).setReceiverCode(delegateTask.getAssignee()).setBizId(delegateTask.getId());
            MqMessageBO nextMqMessageBO = new MqMessageBO(Constants.TOPIC_OA_TODO_DONE, Constants.TAG_OA_TODO_DONE, JSON.toJSONString(processDoneRequest) );
            nextMqMessageBO = mqMessageSendApi.prepare(nextMqMessageBO);
            mqMessageSendApi.send(nextMqMessageBO);
            //发送下一节点处理人代办消息
            List<Task> list = taskService.createTaskQuery().active().processInstanceId(historicProcessInstance.getId()).list();
            list.forEach(todoTask -> {
            /*    ReceiveTodoRequest recvTodoRequest = new ReceiveTodoRequest();
                recvTodoRequest.setBizId(todoTask.getId());
                recvTodoRequest.setCreaterCode(historicProcessInstance.getStartUserId());
                recvTodoRequest.setCreateTime(historicProcessInstance.getStartTime());
                recvTodoRequest.setAppUrl("");
                String nexturl = pcUrl+todoTask.getId();
                recvTodoRequest.setPcUrl(nexturl);
                recvTodoRequest.setReceiverCode(todoTask.getAssignee());
                recvTodoRequest.setReceiveTime(new Date());
                String nextTitle = historicProcessInstance.getProcessDefinitionName()+"-"+employeeDTO.getEmpName()+"-"+ DateUtil.format(historicProcessInstance.getStartTime(), DatePattern.NORM_DATE_PATTERN)+"("+historicProcessInstance.getBusinessKey()+")";
                recvTodoRequest.setTitle(nextTitle);
                recvTodoRequest.setWorkflowName(historicProcessInstance.getProcessDefinitionName());
                MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_OA_TODO_RECV, Constants.TAG_OA_TODO_RECV, JSON.toJSONString(recvTodoRequest) );
                mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
                mqMessageSendApi.send(mqMessageBO);*/
                wfProcessService.sendOaTodoMsg(historicProcessInstance.getId(),todoTask.getId(),historicProcessInstance.getStartTime(),historicProcessInstance.getStartUserId(),todoTask.getAssignee(),historicProcessInstance.getProcessDefinitionName(),employeeDTO.getEmpName(),historicProcessInstance.getBusinessKey(),formDTO.getType(),formDTO.getId(),false,todoTask.getName());
            });
        }
    }
}

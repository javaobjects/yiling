package com.yiling.workflow.workflow.mq.listener;

import java.util.List;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.oa.todo.dto.request.ProcessDoneRequest;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.workflow.workflow.enums.FlowCommentEnum;
import com.yiling.workflow.workflow.service.WfProcessService;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RocketMqListener(topic = Constants.TOPIC_AUTO_APPROVE, consumerGroup = Constants.TOPIC_AUTO_APPROVE)
public class TaskAutoApproveListener  extends AbstractMessageListener {
    @Resource
    protected TaskService taskService;

    @DubboReference
    private MqMessageSendApi mqMessageSendApi;

    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    private FormApi formApi;
    @Resource
    protected RuntimeService runtimeService;
    @Autowired
    private WfProcessService wfProcessService;
    @Resource
    protected HistoryService historyService;

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        log.info("TaskAutoApproveListener MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), body);
        Task task = taskService.createTaskQuery().taskId(body).singleResult();
        taskService.setAssignee(task.getId(), task.getAssignee());
        taskService.addComment(task.getId(), task.getProcessInstanceId(), FlowCommentEnum.AUTO_PASS.getType(),"");
        taskService.complete(task.getId());

        //消息变为已办
        ProcessDoneRequest processDoneRequest = new ProcessDoneRequest();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        FormDTO formDTO = formApi.getByFlowId(historicProcessInstance.getId());
        EsbEmployeeDTO employeeDTO = esbEmployeeApi.getByEmpId(historicProcessInstance.getStartUserId());
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
        return   MqAction.CommitMessage;
    }

    @Override
    protected int getMaxReconsumeTimes() {
        return 0;
    }

    @Override
    protected IConsumeFailureHandler getConsumeFailureHandler() {
        return null;
    }
}

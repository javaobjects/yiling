package com.yiling.workflow.workflow.mq.listener;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.collect.Lists;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.oa.todo.api.OaTodoApi;
import com.yiling.sjms.oa.todo.dto.request.ReceiveTodoRequest;
import com.yiling.sjms.util.OaTodoUtils;
import com.yiling.workflow.workflow.constant.FlowConstant;
import com.yiling.workflow.workflow.entity.ActRuTaskDO;
import com.yiling.workflow.workflow.entity.WfActHistoryDO;
import com.yiling.workflow.workflow.service.ActRuTaskService;
import com.yiling.workflow.workflow.service.WfActHistoryService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 流程转发操作历史记录消费
 *
 * @author: xuan.zhou
 * @date: 2023/2/15
 */
@Slf4j
@RocketMqListener(topic = FlowConstant.TOPIC_WF_ACT_FORWARD, consumerGroup = FlowConstant.TOPIC_WF_ACT_FORWARD)
public class WfActForwardHistoryListener extends AbstractMessageListener {

    @Value("${oa.sso.url:123}")
    private String oaSsoUrl;
    @Value("${oa.todo.pcUrl:123}")
    private String oaTodoPcUrl;
    @Value("${oa.todo.appUrl:123}")
    private String oaTodoAppUrl;

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;
    @DubboReference
    OaTodoApi oaTodoApi;
    @DubboReference
    FormApi formApi;

    @Autowired
    WfActHistoryService wfActHistoryService;
    @Autowired
    TaskService taskService;
    @Autowired
    HistoryService historyService;

    @Autowired
    ActRuTaskService actRuTaskService;


    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        Long id = Convert.toLong(body);

        WfActHistoryDO wfActHistoryDO = wfActHistoryService.getById(id);
        if (wfActHistoryDO == null) {
            log.error("未找到转发操作历史记录。id={}", id);
            return MqAction.CommitMessage;
        }

        FormDTO formDTO = formApi.getById(wfActHistoryDO.getFormId());

        List<String> toEmpIds = Arrays.stream(StrUtil.split(wfActHistoryDO.getToEmpIds(), ",")).collect(Collectors.toList());
        if (CollUtil.isEmpty(toEmpIds)) {
            log.error("转发员工ID列表为空。id={}", id);
            return MqAction.CommitMessage;
        }

        ReceiveTodoRequest request = new ReceiveTodoRequest();
        request.setBizId(formDTO.getCode());
        request.setFormCode(formDTO.getCode());
        request.setTitle(formDTO.getName());
        request.setWorkflowName(FormTypeEnum.getByCode(formDTO.getType()).getName());
        request.setNodeName("转发");
        request.setCreaterCode(wfActHistoryDO.getFromEmpId());
        request.setCreateTime(new Date());
        request.setReceiveTime(new Date());

        FormTypeEnum formTypeEnum = FormTypeEnum.getByCode(formDTO.getType());
        request.setPcUrl(OaTodoUtils.genPcUrl(oaSsoUrl, oaTodoPcUrl, formTypeEnum, formDTO.getId(), id));
        request.setAppUrl(OaTodoUtils.genAppUrl(oaSsoUrl, oaTodoAppUrl, formTypeEnum, formDTO.getId(), id));
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(formDTO.getCode()).singleResult();
        List<ActRuTaskDO> actRuTaskDOList = Lists.newArrayListWithExpectedSize(toEmpIds.size());
        toEmpIds.forEach(e -> {
            request.setReceiverCode(e);
            oaTodoApi.receiveTodo(request);
            //插入待办任务
            ActRuTaskDO actRuTaskDO = new ActRuTaskDO();
            actRuTaskDO.setAssignee(e).setProcInstId(historicProcessInstance.getId()).setProcDefId(historicProcessInstance.getProcessDefinitionId()).setName("转发").setSuspensionState(1).setIsCountEnabled(1)
                    .setVarCount(0).setIdLinkCount(0).setSubTaskCount(0).setCreateTime(new Date()).setId(UUID.randomUUID().toString()).setExecutionId(id.toString()+"-"+e).setTaskDefKey("");
            actRuTaskDOList.add(actRuTaskDO);
        });
        actRuTaskService.batchInsert(actRuTaskDOList);
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

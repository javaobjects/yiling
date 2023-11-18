package com.yiling.workflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngines;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.sjms.constant.ApproveConstant;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.oa.todo.api.OaTodoApi;
import com.yiling.sjms.oa.todo.dto.request.ReceiveTodoRequest;
import com.yiling.workflow.workflow.api.ProcessApi;
import com.yiling.workflow.workflow.dto.ProcessDetailDTO;
import com.yiling.workflow.workflow.dto.WorkFlowNodeDTO;
import com.yiling.workflow.workflow.dto.request.GetProcessDetailRequest;
import com.yiling.workflow.workflow.dto.request.StartGroupBuyRequest;
import com.yiling.workflow.workflow.dto.request.StartProcessRequest;
import com.yiling.workflow.workflow.entity.ActHiTaskinstDO;
import com.yiling.workflow.workflow.entity.ActRuExecutionDO;
import com.yiling.workflow.workflow.entity.ActRuTaskDO;
import com.yiling.workflow.workflow.entity.WfActHistoryDO;
import com.yiling.workflow.workflow.enums.FlowCommentEnum;
import com.yiling.workflow.workflow.service.ActHiTaskinstService;
import com.yiling.workflow.workflow.service.ActRuExecutionService;
import com.yiling.workflow.workflow.service.ActRuTaskService;
import com.yiling.workflow.workflow.service.WfActHistoryService;
import com.yiling.workflow.workflow.service.WfProcessService;
import com.yiling.workflow.workflow.service.WfTaskService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: gxl
 * @date: 2022/11/29
 */
@Slf4j
public class ProcessTest extends BaseTest {
    @DubboReference(url = "dubbo://localhost:17021")
    ProcessApi processApi;

    @Autowired
    WfTaskService wfTaskService;
    @Autowired
    WfProcessService wfProcessService;
    @Resource
    protected HistoryService historyService;

    @DubboReference
    private OaTodoApi oaTodoApi;

    @Value("#{${common.gb.process-parameter}}")
    Map<String, String> process ;


    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;
    @Autowired
    WfActHistoryService wfActHistoryService;
    @DubboReference
    FormApi formApi;
    @Autowired
    TaskService taskService;
    @Autowired
    ActRuTaskService actRuTaskService;

    @Autowired
    ActHiTaskinstService actHiTaskinstService;
    @Autowired
    ActRuExecutionService actRuExecutionService;
    /**
     * 发起流程
     */
    @Test
    public void test1(){
        StartGroupBuyRequest request = new StartGroupBuyRequest();
        request.setBusinessKey("tg123456").setOrgId(13029L).setProvinceName("河南").setProcDefId("Process_1669550166428:5:a296aded-75ce-11ed-9422-0271cc946657").setStartUserId("24154");
        request.setGovernmentBuy(true).setTerminalCompanyCode(new ArrayList<String>() {{
            add("BZJ000333");
        }}).setGbId(1L);
        processApi.startGroupBuyProcess(request);
    }
    @Test
    public void test9(){
        StartGroupBuyRequest request = new StartGroupBuyRequest();
        request.setBusinessKey("tg123456").setOrgId(13029L).setProvinceName("河南").setProcDefId("Process_1669550166428:5:a296aded-75ce-11ed-9422-0271cc946657").setStartUserId("24154");
      //  request.setGovernmentBuy(true).setTerminalCompanyCode("BZJ000333").setGbId(1L);
        StartProcessRequest startProcessRequest = new StartProcessRequest();
        HashMap<String, Object> var = Maps.newHashMap();
        var.put(ApproveConstant.BUSINESS_DATA_SECTION,process.get(ApproveConstant.BUSINESS_DATA_SECTION));
        startProcessRequest.setVariables(var);
        startProcessRequest.setFormType(FormTypeEnum.EXTEND_INFO_CHANGE.getCode()).setStartUserId("30928").setProcDefId("holidayRequest:13:07cabc9e-bf27-11ed-b7aa-0271cc946657");
        startProcessRequest.setBusinessKey("EC20230303171446187212");
        wfProcessService.startProcess(startProcessRequest);
    }
    @Test
    public void test10(){
        StartGroupBuyRequest request = new StartGroupBuyRequest();
        StartProcessRequest startProcessRequest = new StartProcessRequest();
        String s="{\"businessKey\":\"EN20230307085855127408\",\"empName\":\"张珈纶\",\"formType\":5,\"id\":3144,\"procDefId\":\"holidayRequest:2:d025f33e-b753-11ed-8e46-0271cc946657\",\"startUserId\":\"02144\",\"tag\":\"tag_extend_change\",\"title\":\"机构解锁-张珈纶-2023-03-07（EN20230307085855127408）\",\"variables\":{\"marketingCompanyGeneralManager\":\"YX00103\",\"digitalGeneralManager\":\"22402\",\"userPlate\":2,\"users\":\"YX04260_30621_30928_22480\",\"mcFinanceDirector\":\"YX03347\",\"superior\":\"YX00904\",\"businessDataSection\":\"09352_11436\",\"commerceProvinceManager\":\"YX04129\",\"aooChief\":\"31090\",\"homDeputyDirector\":\"29259\",\"buDirector\":\"YX00204\",\"cmdGeneralManager\":\"22307\",\"executiveDeputyGeneralManager\":\"YX00902\",\"commerceDeptDirector\":\"23568\"}}";
        startProcessRequest = JSON.parseObject(s,StartProcessRequest.class);
        wfProcessService.startProcess(startProcessRequest);
    }

    /**
     * 驳回
     */
    @Test
    public void test2(){
        List<WorkFlowNodeDTO> returnTaskList = wfTaskService.findReturnTaskList("f7391d2c-7060-11ed-9739-0271cc946657");
        System.out.println(JSON.toJSONString(returnTaskList));
    }
    /**
     * 部署流程
     */
    @Test
    public void test3(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("processes/groupBuy.bpmn")
                .name("团购提报")
                .category("1") // 分类
                .tenantId("1") // 租户id
                .deploy();
        System.out.println("deploy.getId() = " + deploy.getId());
        System.out.println("deploy.getName() = " + deploy.getName());
        System.out.println("deploy.getCategory() = " + deploy.getCategory());
    }

    /**
     * 重新发起
     */
    @Test
    public void test4(){
        wfTaskService.reSubmit("tg12345",null);
    }

    /**
     * 流程审批记录
     */
    @Test
    public void test5(){
        GetProcessDetailRequest req = new GetProcessDetailRequest();
        req.setBusinessKey("tg123456");
        List<ProcessDetailDTO> processDetail = processApi.getProcessDetail(req);
        System.out.println(JSON.toJSONString(processDetail));
    }


   /* public  void test6(){
        HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().processInstanceId("cdb9e8e2-84ea-11ed-9c22-00163e00bab1").finished().singleResult();
        ReceiveTodoRequest receiveTodoRequest = new ReceiveTodoRequest();
        receiveTodoRequest.setBizId(taskInstance.getId());
        receiveTodoRequest.setCreaterCode("24154");
        receiveTodoRequest.setCreateTime(taskInstance.getCreateTime());
        receiveTodoRequest.setAppUrl("");
        String url = pcUrl+taskInstance.getId();

        receiveTodoRequest.setPcUrl(url);
        receiveTodoRequest.setReceiverCode("24154");
        receiveTodoRequest.setReceiveTime(new Date());
       // MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_OA_TODO, Constants.TAG_OA_TODO, JSON.toJSONString(receiveTodoRequest) );
        String title = "团购提报"+"-"+"高新"+"-"+ DateUtil.format(taskInstance.getCreateTime(), DatePattern.NORM_DATE_PATTERN);
        receiveTodoRequest.setTitle(title);
        receiveTodoRequest.setWorkflowName("团购提报");
        log.info(receiveTodoRequest.toString());
        boolean b = oaTodoApi.receiveTodo(receiveTodoRequest);
        System.out.println(b);
    }
*/
    @Test
    public void test11(){
        Long id =135l;
        WfActHistoryDO wfActHistoryDO = wfActHistoryService.getById(id);
        if (wfActHistoryDO == null) {
            log.error("未找到转发操作历史记录。id={}", id);
        }

        FormDTO formDTO = formApi.getById(wfActHistoryDO.getFormId());

        List<String> toEmpIds = Arrays.stream(StrUtil.split(wfActHistoryDO.getToEmpIds(), ",")).collect(Collectors.toList());
        if (CollUtil.isEmpty(toEmpIds)) {
            log.error("转发员工ID列表为空。id={}", id);
        }

        ReceiveTodoRequest request = new ReceiveTodoRequest();
        request.setBizId(formDTO.getCode());
        request.setTitle(formDTO.getName());
        request.setAppUrl("");
        request.setWorkflowName(FormTypeEnum.getByCode(formDTO.getType()).getName());
        request.setCreaterCode(wfActHistoryDO.getFromEmpId());
        request.setCreateTime(new Date());
        request.setReceiveTime(new Date());

        FormTypeEnum formTypeEnum = FormTypeEnum.getByCode(formDTO.getType());
        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery().processInstanceId(formDTO.getFlowId()).orderByHistoricTaskInstanceStartTime().desc().list();
        String taskId = "0";
        if (CollUtil.isNotEmpty(taskList)) {
            taskId = taskList.get(0).getId();
        }
       // request.setPcUrl(OaTodoUtils.genPcUrl(oaSsoUrl, oaTodoPcUrl, formTypeEnum, formDTO.getId(), id));
    }

    @Test
    public void test12(){
        List<Comment> commentList = taskService.getProcessInstanceComments("62cc81cc-cc6b-11ed-ba9f-00163e15118b");
        List<Comment> rejectCommentList = commentList.stream().filter(comment -> FlowCommentEnum.REBACK.getType().equals(comment.getType())).sorted(Comparator.comparing(Comment::getTime)).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(rejectCommentList));
    }

    @Test
    public void test13(){
        LambdaQueryWrapper<ActRuTaskDO> wrapper = Wrappers.lambdaQuery();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey("HM20230519161342007323").singleResult();
        wrapper.eq(ActRuTaskDO::getAssignee,"24076").eq(ActRuTaskDO::getProcInstId,historicProcessInstance.getId()).eq(ActRuTaskDO::getExecutionId,617).last("limit 1");
        ActRuTaskDO actRuTaskDO = actRuTaskService.getOne(wrapper);
        ActRuExecutionDO actRuExecutionDO = new ActRuExecutionDO();
        actRuExecutionDO.setId("617").setParentId(null);
        actRuTaskService.removeById(actRuTaskDO.getId());
        actRuExecutionService.updateById(actRuExecutionDO);
        actRuExecutionService.removeById("617");

        ActHiTaskinstDO actHiTaskinstDO = new ActHiTaskinstDO();
        actHiTaskinstDO.setId(actRuTaskDO.getId());
        actHiTaskinstDO.setEndTime(new Date()).setLastUpdatedTime(new Date());
        actHiTaskinstService.updateById(actHiTaskinstDO);
    }
}
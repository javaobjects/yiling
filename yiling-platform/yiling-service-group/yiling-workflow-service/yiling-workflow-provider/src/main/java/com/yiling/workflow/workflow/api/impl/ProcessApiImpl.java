package com.yiling.workflow.workflow.api.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.gb.api.GbWorkflowProcessorApi;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.api.EsbOrganizationApi;
import com.yiling.user.esb.bo.SimpleEsbEmployeeInfoBO;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.request.QueryProvinceManagerRequest;
import com.yiling.workflow.workflow.api.ProcessApi;
import com.yiling.workflow.workflow.constant.FlowConstant;
import com.yiling.workflow.workflow.dto.ProcessDetailDTO;
import com.yiling.workflow.workflow.dto.ProcessInstanceDTO;
import com.yiling.workflow.workflow.dto.WfTaskDTO;
import com.yiling.workflow.workflow.dto.request.GetProcessDetailRequest;
import com.yiling.workflow.workflow.dto.request.QueryFinishedProcessPageRequest;
import com.yiling.workflow.workflow.dto.request.QueryTodoProcessPageRequest;
import com.yiling.workflow.workflow.dto.request.StartGroupBuyRequest;
import com.yiling.workflow.workflow.dto.request.StartProcessRequest;
import com.yiling.workflow.workflow.service.WfProcessService;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 流程
 * @author: gxl
 * @date: 2022/11/28
 */
@Slf4j
@RefreshScope
@DubboService
public class ProcessApiImpl implements ProcessApi {

    @Resource
    protected IdentityService identityService;

    @Autowired
    private WfProcessService wfProcessService;

    @DubboReference
    private GbWorkflowProcessorApi gbWorkflowProcessorApi;

    @Resource
    protected HistoryService historyService;

    @Resource
    protected TaskService taskService;

    @DubboReference
    private CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;

    @DubboReference
    private MqMessageSendApi mqMessageSendApi;

    @DubboReference
    private EsbOrganizationApi esbOrganizationApi;

    @Value("#{${common.gb.process-parameter}}")
    Map<String, String> gbProcess ;

    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;
    @Override
    public Page<WfTaskDTO> queryTodoProcessList(QueryTodoProcessPageRequest request) {
        return wfProcessService.queryPageTodoProcessList(request);
    }

    @Override
    public Page<WfTaskDTO> queryFinishedProcessList(QueryFinishedProcessPageRequest request) {
        return wfProcessService.queryFinishedProcessList(request);
    }

    @Override
    public void startGroupBuyProcess(StartGroupBuyRequest request) {
        //查询市场运营部对应省区负责人
        QueryProvinceManagerRequest queryProvinceManagerRequest = new QueryProvinceManagerRequest();
        queryProvinceManagerRequest.setOrgId(FlowConstant.MARKET_MANAGER_ORG_ID).setProvinceName(request.getProvinceName());
        SimpleEsbEmployeeInfoBO employeeInfoBO = gbWorkflowProcessorApi.getByProvinceName(queryProvinceManagerRequest);
        if(Objects.isNull(employeeInfoBO)){
            throw new BusinessException(ResultCode.FAILED,"获取市场运营部对应省区的省区经理信息失败");
        }
        //查商务部对应省区负责人
        QueryProvinceManagerRequest queryMarketRequest = new QueryProvinceManagerRequest();
        queryMarketRequest.setOrgId(FlowConstant.COMMERCE_ORG_ID).setProvinceName(request.getProvinceName());
        SimpleEsbEmployeeInfoBO marketEmployeeInfoBO = gbWorkflowProcessorApi.getByProvinceName(queryMarketRequest);
        if(Objects.isNull(marketEmployeeInfoBO)){
            throw new BusinessException(ResultCode.FAILED,"获取商务部对应省区的省区经理信息失败");
        }
        //查询上级部门
        SimpleEsbEmployeeInfoBO deptLeader = gbWorkflowProcessorApi.getByOrgId(request.getOrgId());
        if(Objects.isNull(deptLeader)){
            throw new BusinessException(ResultCode.FAILED,"获取发起人部门领导信息失败");
        }
        StartProcessRequest start = new StartProcessRequest();
        Map<String,Object> var = Maps.newHashMap();
        var.put("startUserId",request.getStartUserId());
        //指定市场运营部省区经理工号
        var.put(FlowConstant.MARKET_PROVINCE_MANAGER,employeeInfoBO.getEmpId());
        //指定部门领导
        var.put(FlowConstant.DEPT_LEADER,deptLeader.getEmpId());
        var.put(FlowConstant.DEPT,request.getOrgId());
        //是否呼吸事业部
        if(request.getOrgId().equals(FlowConstant.BREATH_DEPT)){
            var.put("breathDept",true);
        }else{
            var.put("breathDept",false);
        }
        //是否政府团购
        var.put("governmentBuy",request.getGovernmentBuy());
        // 出货终端是否是事业三部
        boolean isBreathDept = false;
        if(CollectionUtil.isNotEmpty(request.getTerminalCompanyCode())){
            isBreathDept = crmEnterpriseRelationShipApi.isBreathingDepartmentByNameCode(request.getTerminalCompanyCode());
        }
        var.put("outDept",isBreathDept);
        //审批人变量设置 配置文件里
        var.put(FlowConstant.RESPIRATORY_BUSINESS_DEPT_DIRECTOR,gbProcess.get(FlowConstant.RESPIRATORY_BUSINESS_DEPT_DIRECTOR));
        //var.put(FlowConstant.COMMERCE_DEPT_DEPUTY_MANAGER,gbProcess.get(FlowConstant.COMMERCE_DEPT_DEPUTY_MANAGER));
        var.put(FlowConstant.COMMERCE_DEPT_DIRECTOR,gbProcess.get(FlowConstant.COMMERCE_DEPT_DIRECTOR));
        var.put(FlowConstant.MARKETING_DIRECTOR,gbProcess.get(FlowConstant.MARKETING_DIRECTOR));
        //var.put(FlowConstant.MARKETING_COMPANY_GENERAL_MANAGER,gbProcess.get(FlowConstant.MARKETING_COMPANY_GENERAL_MANAGER));
        var.put(FlowConstant.PUBLIC_AFFAIRS_DIRECTOR,gbProcess.get(FlowConstant.PUBLIC_AFFAIRS_DIRECTOR));
        var.put(FlowConstant.OPERATIONS_MANAGER,gbProcess.get(FlowConstant.OPERATIONS_MANAGER));
        var.put(FlowConstant.GB_ID,request.getGbId());
        var.put(FlowConstant.COMMERCE_PROVINCE_MANAGER,marketEmployeeInfoBO.getEmpId());
        EsbEmployeeDTO employeeDTO = esbEmployeeApi.getByEmpId(request.getStartUserId());
        var.put("superior",employeeDTO.getSuperior());
        var.put(FlowConstant.FINANCE,gbProcess.get(FlowConstant.FINANCE));
        //设置发起人
        identityService.setAuthenticatedUserId(request.getStartUserId());
        var.put(BpmnXMLConstants.ATTRIBUTE_EVENT_START_INITIATOR, request.getStartUserId());
        start.setBusinessKey(request.getBusinessKey()).setProcDefId(request.getProcDefId()).setVariables(var).setStartUserId(request.getStartUserId());
        start.setEmpName(employeeDTO.getEmpName()).setId(request.getGbId());
        start.setFormType(FormTypeEnum.GB_SUBMIT.getCode());
        ProcessInstanceDTO processInstanceDTO = wfProcessService.startProcess(start);
        identityService.setAuthenticatedUserId(null);
        if(Objects.nonNull(processInstanceDTO)){
/*            HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceDTO.getProcessInstanceId()).finished().singleResult();
            ReceiveTodoRequest receiveTodoRequest = new ReceiveTodoRequest();
            receiveTodoRequest.setBizId(taskInstance.getId());
            receiveTodoRequest.setCreaterCode(request.getStartUserId());
            receiveTodoRequest.setCreateTime(taskInstance.getCreateTime());
            receiveTodoRequest.setAppUrl("");
            String url = pcUrl+taskInstance.getId();
            receiveTodoRequest.setPcUrl(url);
            receiveTodoRequest.setReceiverCode(request.getStartUserId());
            receiveTodoRequest.setReceiveTime(new Date());
            receiveTodoRequest.setAutoDone(true);
            String title = processInstanceDTO.getProcessDefinitionName()+"-"+employeeDTO.getEmpName()+"-"+ DateUtil.format(taskInstance.getCreateTime(), DatePattern.NORM_DATE_PATTERN)+"("+request.getBusinessKey()+")";
            receiveTodoRequest.setTitle(title);
            receiveTodoRequest.setWorkflowName(processInstanceDTO.getProcessDefinitionName());
            MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_OA_TODO_RECV, Constants.TAG_OA_TODO_RECV, JSON.toJSONString(receiveTodoRequest) );
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
            //发起人我的待办
            mqMessageSendApi.send(mqMessageBO);*/
           /* List<Task> list = taskService.createTaskQuery().active().processInstanceId(processInstanceDTO.getProcessInstanceId()).list();
            list.forEach(task -> {
                ReceiveTodoRequest recvTodoRequest = new ReceiveTodoRequest();
                recvTodoRequest.setBizId(task.getId());
                recvTodoRequest.setCreaterCode(request.getStartUserId());
                recvTodoRequest.setCreateTime(processInstanceDTO.getStartTime());
                recvTodoRequest.setAppUrl("");
                String nexturl = pcUrl+task.getId();
                recvTodoRequest.setPcUrl(nexturl);
                recvTodoRequest.setReceiverCode(task.getAssignee());
                recvTodoRequest.setReceiveTime(new Date());
                String nextTitle = processInstanceDTO.getProcessDefinitionName()+"-"+employeeDTO.getEmpName()+"-"+ DateUtil.format(processInstanceDTO.getStartTime(), DatePattern.NORM_DATE_PATTERN)+"("+request.getBusinessKey()+")";
                recvTodoRequest.setTitle(nextTitle);
                recvTodoRequest.setWorkflowName(processInstanceDTO.getProcessDefinitionName());
                MqMessageBO nextMqMessageBO = new MqMessageBO(Constants.TOPIC_OA_TODO_RECV, Constants.TAG_OA_TODO_RECV, JSON.toJSONString(recvTodoRequest) );
                nextMqMessageBO = mqMessageSendApi.prepare(nextMqMessageBO);
                //发起人我的待办
                mqMessageSendApi.send(nextMqMessageBO);
            });*/
        }

    }

    @Override
    public List<ProcessDetailDTO> getProcessDetail(GetProcessDetailRequest req) {
        return  wfProcessService.getProcessDetail(req);
    }

    @Override
    public List<ProcessDetailDTO> getProcessDetailByInsId(String proInsId) {
        return wfProcessService.getProcessDetailByInsId(proInsId);
    }
}
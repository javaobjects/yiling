package com.yiling.sjms.workflow.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.gb.api.GbMainInfoApi;
import com.yiling.sjms.gb.dto.request.UpdateMainInfoReviewTypeRequest;
import com.yiling.sjms.gb.vo.GbProcessDetailVO;
import com.yiling.sjms.workflow.form.AuditTaskForm;
import com.yiling.sjms.workflow.form.CommentForm;
import com.yiling.sjms.workflow.form.ForwardForm;
import com.yiling.sjms.workflow.vo.WfActHistoryVO;
import com.yiling.sjms.workflow.vo.WorkFlowNodeVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.workflow.workflow.api.ProcessApi;
import com.yiling.workflow.workflow.api.TaskApi;
import com.yiling.workflow.workflow.dto.ProcessDetailDTO;
import com.yiling.workflow.workflow.dto.WfActHistoryDTO;
import com.yiling.workflow.workflow.dto.WfTaskDTO;
import com.yiling.workflow.workflow.dto.WorkFlowNodeDTO;
import com.yiling.workflow.workflow.dto.request.AddCommentHistoryRequest;
import com.yiling.workflow.workflow.dto.request.AddForwardHistoryRequest;
import com.yiling.workflow.workflow.dto.request.CompleteWfTaskRequest;
import com.yiling.workflow.workflow.enums.WfActTypeEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: gxl
 * @date: 2022/11/30
 */
@Slf4j
@RestController
@RequestMapping("/task")
@Api(tags = "流程中心-任务")
public class TaskController extends BaseController {
    @DubboReference
    FormApi formApi;

    @DubboReference
    TaskApi taskApi;

    @DubboReference
    GbMainInfoApi gbMainInfoApi;

    @DubboReference
    private ProcessApi processApi;

    @ApiOperation(value = "通用审批")
    @PostMapping("audit")
    public Result<Boolean> audit(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestBody @Valid AuditTaskForm auditTaskForm){
        CompleteWfTaskRequest request = new CompleteWfTaskRequest();
        PojoUtils.map(auditTaskForm,request);
        request.setAssignee(userInfo.getCurrentUserCode()).setOpUserId(userInfo.getCurrentUserId());
        if(Objects.nonNull(auditTaskForm.getGbReviewType()) && Objects.nonNull(auditTaskForm.getGbCityBelow()) &&auditTaskForm.getGbReviewType()>0 && auditTaskForm.getGbCityBelow()>0){
            UpdateMainInfoReviewTypeRequest infoRequest = new UpdateMainInfoReviewTypeRequest();
            infoRequest.setGbId(auditTaskForm.getFormId()).setGbCityBelow(auditTaskForm.getGbCityBelow()).setGbReviewType(auditTaskForm.getGbReviewType());
            gbMainInfoApi.updateByGbId(infoRequest);
            HashMap map = Maps.newHashMap();
            map.put("governmentBuy",auditTaskForm.getGbReviewType() == 1 ? false:true);
            request.setVariables(map);
        }
        WfTaskDTO taskDTO = taskApi.getByFormId(auditTaskForm.getFormId(), userInfo.getCurrentUserCode(),0L);
        if(Objects.isNull(taskDTO)){
           return Result.failed("此节点已完成审批，操作失败");
        }
        request.setTaskId(taskDTO.getTaskId()).setGbId(auditTaskForm.getFormId());
        if(auditTaskForm.getIsAgree()){
            taskApi.completeTask(request);
        }else{
            taskApi.returnTask(request);
        }
        return Result.success(true);
    }
    @ApiOperation(value = "转发")
    @PostMapping("/forward")
    public Result<Void> forward(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Validated ForwardForm form){
        AddForwardHistoryRequest request = new AddForwardHistoryRequest();
        request.setFormId(form.getFormId());
        request.setFromEmpId(userInfo.getCurrentUserCode());
        request.setToEmpIds(form.getToEmpIds());
        request.setText(form.getText());
        request.setOpUserId(userInfo.getCurrentUserId());
        taskApi.forward(request);
        return Result.success();
    }

    @ApiOperation(value = "批注")
    @PostMapping("/comment")
    public Result<Void> comment(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Validated CommentForm form){
        AddCommentHistoryRequest request = new AddCommentHistoryRequest();
        request.setForwardHistoryId(form.getForwardHistoryId());
        request.setFormId(form.getFormId());
        request.setFromEmpId(userInfo.getCurrentUserCode());
        request.setText(form.getText());
        request.setOpUserId(userInfo.getCurrentUserId());
        taskApi.comment(request);
        return Result.success();
    }

    @ApiOperation(value = "根据表单ID获取操作历史记录")
    @GetMapping("/history")
    public Result<CollectionObject<WfActHistoryVO>> history(
            @CurrentUser CurrentSjmsUserInfo userInfo,
            @ApiParam(value = "表单ID", required = true) @RequestParam Long formId){

        List<WfActHistoryDTO> wfActHistoryDTOList = taskApi.listHistoryByFormId(formId);
        if (CollUtil.isEmpty(wfActHistoryDTOList)) {
            return Result.success(new CollectionObject<>(ListUtil.empty()));
        }

        List<WfActHistoryVO> wfActHistoryVOList = CollUtil.newArrayList();
        for (WfActHistoryDTO wfActHistoryDTO : wfActHistoryDTOList) {
            WfActTypeEnum wfActTypeEnum = WfActTypeEnum.getByCode(wfActHistoryDTO.getActType());

            WfActHistoryVO wfActHistoryVO = new WfActHistoryVO();
            wfActHistoryVO.setFromEmpName(wfActHistoryDTO.getFromEmpName());
            wfActHistoryVO.setToEmpNames(wfActHistoryDTO.getToEmpNames());
            wfActHistoryVO.setActTypeName(wfActTypeEnum.getName());
            wfActHistoryVO.setActTextTypeName(wfActTypeEnum.getTextTypeName());
            wfActHistoryVO.setActText(wfActHistoryDTO.getActText());
            wfActHistoryVO.setActTime(wfActHistoryDTO.getActTime());

            wfActHistoryVOList.add(wfActHistoryVO);
        }

        return Result.success(new CollectionObject<>(wfActHistoryVOList));
    }
    @ApiOperation(value = "根据表单id获取审批记录")
    @GetMapping("/queryAuditHistory")
    public Result<CollectionObject<GbProcessDetailVO>> queryAuditHistory(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam Long formId){
        FormDTO formDTO = formApi.getById(formId);
        List<ProcessDetailDTO> processDetail = processApi.getProcessDetailByInsId(formDTO.getFlowId());
        List<GbProcessDetailVO> gbProcessDetailList = PojoUtils.map(processDetail, GbProcessDetailVO.class);
        return Result.success(new CollectionObject<>(gbProcessDetailList));
    }
    /**
     * 查看实例流程图，根据流程实例ID获取流程图
     */
    @ApiOperation(value = "流程图")
    @RequestMapping(value="traceProcess/{instanceId}",method= RequestMethod.GET)
    public Result<String> traceProcess(@PathVariable("instanceId")String instanceId) {
      String pic =  taskApi.genProcessDiagram(instanceId);
      return Result.success("data:image/png;base64,"+pic);
    }
    @ApiOperation(value = "获取可驳回节点")
    @GetMapping("queryReturnTaskList")
    public Result<CollectionObject<WorkFlowNodeVO>> findReturnTaskList(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam Long formId){
        WfTaskDTO taskDTO = taskApi.getByFormId(formId, userInfo.getCurrentUserCode(),0L);
        if(Objects.isNull(taskDTO)){
           return Result.success(new CollectionObject(null));
        }
        List<WorkFlowNodeDTO> returnTaskList = taskApi.findReturnTaskList(taskDTO.getTaskId());
        if(CollUtil.isNotEmpty(returnTaskList)){
            //移除发起人
            returnTaskList.remove(0);
        }
        List<WorkFlowNodeVO> workFlowNodeVOS = PojoUtils.map(returnTaskList,WorkFlowNodeVO.class);
        CollectionObject collectionObject = new CollectionObject(workFlowNodeVOS);
        return Result.success(collectionObject);
    }
}
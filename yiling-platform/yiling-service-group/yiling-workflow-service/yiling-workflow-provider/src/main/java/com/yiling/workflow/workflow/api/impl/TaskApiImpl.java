package com.yiling.workflow.workflow.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.workflow.workflow.api.TaskApi;
import com.yiling.workflow.workflow.dto.CommentDTO;
import com.yiling.workflow.workflow.dto.WfActHistoryDTO;
import com.yiling.workflow.workflow.dto.WfTaskDTO;
import com.yiling.workflow.workflow.dto.WorkFlowNodeDTO;
import com.yiling.workflow.workflow.dto.request.AddCommentHistoryRequest;
import com.yiling.workflow.workflow.dto.request.AddForwardHistoryRequest;
import com.yiling.workflow.workflow.dto.request.CommonResubmitRequest;
import com.yiling.workflow.workflow.dto.request.CompleteWfTaskRequest;
import com.yiling.workflow.workflow.dto.request.GetTaskDetailRequest;
import com.yiling.workflow.workflow.service.WfTaskService;

/**
 * 任务
 * @author: gxl
 * @date: 2022/11/30
 */
@DubboService
public class TaskApiImpl implements TaskApi {

    @Autowired
    private WfTaskService wfTaskService;

    @Override
    public void completeTask(CompleteWfTaskRequest request) {
        wfTaskService.completeTask(request);
    }

    @Override
    public void returnTask(CompleteWfTaskRequest request) {
        wfTaskService.returnTask(request);
    }

    @Override
    public void reSubmit(String businessKey, Map<String, Object> variables) {
        wfTaskService.reSubmit(businessKey,variables);
    }

    @Override
    public void commonReSubmit(CommonResubmitRequest commonResubmitRequest) {

    }

    @Override
    public String genProcessDiagram(String instanceId) {
        return wfTaskService.genProcessDiagram(instanceId);
    }

    @Override
    public WfTaskDTO getById(GetTaskDetailRequest request) {
        return wfTaskService.getById(request);
    }

    @Override
    public void forward(AddForwardHistoryRequest request) {
        wfTaskService.forward(request);
    }

    @Override
    public void comment(AddCommentHistoryRequest request) {
        wfTaskService.comment(request);
    }

    @Override
    public List<WfActHistoryDTO> listHistoryByFormId(Long formId) {
        return wfTaskService.listHistoryByForm(formId);
    }

    @Override
    public WfTaskDTO getByFormId(Long formId, String userCode,Long forwardHistoryId) {
        return wfTaskService.getByFormId(formId,userCode,forwardHistoryId);
    }

    @Override
    public List<WorkFlowNodeDTO> findReturnTaskList(String taskId) {
        return wfTaskService.findReturnTaskList(taskId);
    }

    @Override
    public CommentDTO queryComment(String code) {
        return wfTaskService.queryComment(code);
    }
}
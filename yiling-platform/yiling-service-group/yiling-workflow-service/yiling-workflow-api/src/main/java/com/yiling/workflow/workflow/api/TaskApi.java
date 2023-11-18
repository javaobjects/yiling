package com.yiling.workflow.workflow.api;

import java.util.List;
import java.util.Map;

import com.yiling.workflow.workflow.dto.CommentDTO;
import com.yiling.workflow.workflow.dto.WfActHistoryDTO;
import com.yiling.workflow.workflow.dto.WfTaskDTO;
import com.yiling.workflow.workflow.dto.WorkFlowNodeDTO;
import com.yiling.workflow.workflow.dto.request.AddCommentHistoryRequest;
import com.yiling.workflow.workflow.dto.request.AddForwardHistoryRequest;
import com.yiling.workflow.workflow.dto.request.CommonResubmitRequest;
import com.yiling.workflow.workflow.dto.request.CompleteWfTaskRequest;
import com.yiling.workflow.workflow.dto.request.GetTaskDetailRequest;

/**
 * @author: gxl
 * @date: 2022/11/30
 */
public interface TaskApi {

    /**
     * 审批
     * @param request
     */
    void completeTask(CompleteWfTaskRequest request);


    /**
     * 驳回
     * @param request
     */
    void returnTask(CompleteWfTaskRequest request);

    /**
     * 驳回后重新提交
     * @param businessKey （比如团购编号）
     */
    void reSubmit(String businessKey, Map<String, Object> variables);

    /**
     * 通用驳回重新提交
     * @param commonResubmitRequest
     */
    void commonReSubmit(CommonResubmitRequest commonResubmitRequest);

    /**
     * 高亮流程图
     * @param instanceId
     * @return
     */
    String genProcessDiagram(String instanceId);

    /**
     * 详情
     * @param request
     * @return
     */
    WfTaskDTO getById(GetTaskDetailRequest request);

    /**
     * 转发
     *
     * @param request
     * @author xuan.zhou
     * @date 2023/2/16
     **/
    void forward(AddForwardHistoryRequest request);

    /**
     * 批注
     *
     * @param request
     * @author xuan.zhou
     * @date 2023/2/16
     **/
    void comment(AddCommentHistoryRequest request);

    /**
     * 获取业务表单对应的操作历史记录
     *
     * @param formId 表单ID
     * @return java.util.List<com.yiling.workflow.workflow.entity.WfActHistoryDO>
     * @author xuan.zhou
     * @date 2023/2/15
     **/
    List<WfActHistoryDTO> listHistoryByFormId(Long formId);

    /**
     * 查询当前用户表单待办任务
     * @param formId
     * @param userCode
     * @return
     */
    WfTaskDTO getByFormId(Long formId,String userCode,Long forwardHistoryId);

    /**
     * 获取可回退节点
     * @param taskId
     * @return
     */
    List<WorkFlowNodeDTO> findReturnTaskList(String taskId);

    /**
     * 查询团购的财务最后一次审批意见
     * @param code
     * @return
     */
    CommentDTO queryComment(String code);
}
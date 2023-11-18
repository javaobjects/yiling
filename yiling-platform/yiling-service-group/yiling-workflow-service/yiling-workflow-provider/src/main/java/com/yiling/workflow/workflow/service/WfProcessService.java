package com.yiling.workflow.workflow.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.workflow.workflow.dto.ProcessDetailDTO;
import com.yiling.workflow.workflow.dto.ProcessInstanceDTO;
import com.yiling.workflow.workflow.dto.WfTaskDTO;
import com.yiling.workflow.workflow.dto.request.GetProcessDetailRequest;
import com.yiling.workflow.workflow.dto.request.QueryFinishedProcessPageRequest;
import com.yiling.workflow.workflow.dto.request.QueryTodoProcessPageRequest;
import com.yiling.workflow.workflow.dto.request.StartProcessRequest;

/**
 * @author: gxl
 * @date: 2022/11/28
 */
public interface WfProcessService {

    /**
     * 待办列表
     * @param request
     * @return
     */
    Page<WfTaskDTO> queryPageTodoProcessList(QueryTodoProcessPageRequest request);

    /**
     * 已办列表
     *
     * @param request
     * @return
     */
    Page<WfTaskDTO> queryFinishedProcessList(QueryFinishedProcessPageRequest request);

    /**
     * 发起流程
     * @param request
     */
    ProcessInstanceDTO startProcess(StartProcessRequest request);

    /**
     * 团购取消发起流程
     * @param request
     */
    void startCancleProcess(StartProcessRequest request);

    /**
     * 审批记录
     * @param req
     * @return
     */
    List<ProcessDetailDTO> getProcessDetail(GetProcessDetailRequest req);

    /**
     * 发送oa待办消息
     * @param processInstanceId 流程实例id
     * @param createrCode 流程发起人工号
     * @param receiverCode 接收人工号
     * @param processDefinitionName 流程名称
     * @param empName 发起人姓名
     * @param businessKey 单据编号
     */
    void sendOaTodoMsg(String processInstanceId, String taskId, Date createTime, String createrCode, String receiverCode, String processDefinitionName, String empName, String businessKey, Integer formType, Long formId,Boolean autoDone,String taskName);

    /**
     * 根据流程实例id查询审批记录
     * @param proInsId
     * @return
     */
    List<ProcessDetailDTO> getProcessDetailByInsId(String proInsId);
}
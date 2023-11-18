package com.yiling.workflow.workflow.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.workflow.workflow.dto.ProcessDetailDTO;
import com.yiling.workflow.workflow.dto.WfTaskDTO;
import com.yiling.workflow.workflow.dto.request.GetProcessDetailRequest;
import com.yiling.workflow.workflow.dto.request.QueryFinishedProcessPageRequest;
import com.yiling.workflow.workflow.dto.request.QueryTodoProcessPageRequest;
import com.yiling.workflow.workflow.dto.request.StartGroupBuyRequest;

/**
 * @author: gxl
 * @date: 2022/11/28
 */
public interface ProcessApi {

    /**
     * 代办列表
     *
     * @param request
     * @return
     */
    Page<WfTaskDTO> queryTodoProcessList(QueryTodoProcessPageRequest request);

    /**
     * 已办列表
     *
     * @param request
     * @return
     */
    Page<WfTaskDTO> queryFinishedProcessList(QueryFinishedProcessPageRequest request);


    /**
     * 团购提交流程启动
     *
     * @param request
     */
    void startGroupBuyProcess(StartGroupBuyRequest request);

    /**
     * 审批记录
     *
     * @param req
     * @return
     */
    List<ProcessDetailDTO> getProcessDetail(GetProcessDetailRequest req);

    /**
     * 根据流程实例id查询审批记录
     * @param proInsId
     * @return
     */
    List<ProcessDetailDTO> getProcessDetailByInsId(String  proInsId);

}

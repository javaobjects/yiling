package com.yiling.sjms.workflow;

import com.yiling.sjms.workflow.dto.request.WorkFlowBaseRequest;

/**
 * @author: gxl
 * @date: 2023/2/25
 */
public interface WorkFlowService {

    /**
     * 发起流程
     * @param request
     */
   void  sendSubmitMsg(WorkFlowBaseRequest request);

    /**
     * 驳回后重新提交
     * @param request
     */
   void resubmitMsg(WorkFlowBaseRequest request);
}

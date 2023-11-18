package com.yiling.workflow.workflow.api;

import com.yiling.workflow.workflow.dto.request.AddWfActHistoryRequest;

/**
 * 流程历史记录 API
 *
 * @author: xuan.zhou
 * @date: 2023/2/15
 */
public interface WfActHistoryApi {

    /**
     * 添加流程操作历史记录
     *
     * @param request 请求对象
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2023/2/15
     **/
    Long addActHistory(AddWfActHistoryRequest request);

    /**
     * 转发记录是否已批注
     *
     * @param commentForwardHistoryId 批注的转发记录ID
     * @param opUserId 操作人ID
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2023/2/20
     **/
    Boolean hasComment(Long commentForwardHistoryId, Long opUserId);
}

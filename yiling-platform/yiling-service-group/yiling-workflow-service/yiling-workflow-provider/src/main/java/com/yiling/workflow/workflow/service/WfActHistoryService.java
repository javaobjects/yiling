package com.yiling.workflow.workflow.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.workflow.workflow.dto.request.AddCommentHistoryRequest;
import com.yiling.workflow.workflow.dto.request.AddForwardHistoryRequest;
import com.yiling.workflow.workflow.dto.request.AddWfActHistoryRequest;
import com.yiling.workflow.workflow.entity.WfActHistoryDO;

/**
 * <p>
 * 流程操作历史记录表 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2023-02-15
 */
public interface WfActHistoryService extends BaseService<WfActHistoryDO> {

    /**
     * 获取业务表单对应的操作历史记录
     *
     * @param formId 表单ID
     * @return java.util.List<com.yiling.workflow.workflow.entity.WfActHistoryDO>
     * @author xuan.zhou
     * @date 2023/3/1
     **/
    List<WfActHistoryDO> listByFormId(Long formId);

    /**
     * 添加转发操作历史记录
     *
     * @param request
     * @return java.lang.Long
     * @author xuan.zhou
     * @date 2023/2/15
     **/
    Long addForwardHistory(AddForwardHistoryRequest request);

    /**
     * 添加批注操作历史记录
     *
     * @param request
     * @return java.lang.Long
     * @author xuan.zhou
     * @date 2023/2/15
     **/
    Long addCommentHistory(AddCommentHistoryRequest request);

    /**
     * 添加流程操作历史记录
     *
     * @param request 请求对象
     * @return java.lang.Boolean 记录ID
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

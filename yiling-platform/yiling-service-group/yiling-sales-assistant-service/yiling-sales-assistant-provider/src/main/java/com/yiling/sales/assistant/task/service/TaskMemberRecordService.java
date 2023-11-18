package com.yiling.sales.assistant.task.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.task.dto.request.QueryTaskMemberPageRequest;
import com.yiling.sales.assistant.task.entity.TaskMemberRecordDO;

/**
 * <p>
 * 会员推广任务记录 服务类
 * </p>
 *
 * @author gxl
 * @date 2021-12-21
 */
public interface TaskMemberRecordService extends BaseService<TaskMemberRecordDO> {

    /**
     * 任务追踪-推广记录
     * @param queryTaskMemberPageRequest
     * @return
     */
    Page<TaskMemberRecordDO> listTaskMemberPage(QueryTaskMemberPageRequest queryTaskMemberPageRequest);

    /**
     * 逻辑删除
     * @param memberRecordDO
     */
   int updateRecordById(TaskMemberRecordDO memberRecordDO);

}

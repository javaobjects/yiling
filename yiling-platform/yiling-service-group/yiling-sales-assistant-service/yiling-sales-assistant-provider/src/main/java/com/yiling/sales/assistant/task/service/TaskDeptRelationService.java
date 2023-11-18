package com.yiling.sales.assistant.task.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.task.entity.TaskDeptRelationDO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gxl
 * @date 2023-01-09
 */
public interface TaskDeptRelationService extends BaseService<TaskDeptRelationDO> {

    /**
     * 通过部门过滤任务
     * @param taskIds
     * @return
     */
    List<Long> filterTaskByDept(List<Long> taskIds,Long eid);
}

package com.yiling.sales.assistant.task.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.task.dto.request.DeleteDeptRequest;
import com.yiling.sales.assistant.task.entity.TaskDeptUserDO;

/**
 * <p>
 * 任务-部门参与人员关系表 服务类
 * </p>
 *
 * @author gxl
 * @date 2021-09-10
 */
public interface TaskDeptUserService extends BaseService<TaskDeptUserDO> {
    /**
     * 移除部门
     * @param request
     * @return
     */
    Boolean deleteDept(DeleteDeptRequest request);
}

package com.yiling.sales.assistant.task.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.task.entity.TaskCustomerCountDO;
import com.yiling.sales.assistant.task.entity.UserTaskDO;

/**
 * <p>
 * 任务拉新户采购额统计表 服务类
 * </p>
 *
 * @author gxl
 * @date 2021-09-24
 */
public interface TaskCustomerCountService extends BaseService<TaskCustomerCountDO> {

    /**
     * 拉户
     * @param taskCustomerCountDO
     * @param inviteUserTask
     * @return
     */
    boolean saveTaskCostumerCount(TaskCustomerCountDO taskCustomerCountDO, UserTaskDO inviteUserTask);

    /**
     * 拉人
     * @param taskCustomerCountDO
     * @param inviteUserTask
     * @return
     */
    boolean saveTaskUserCount(TaskCustomerCountDO taskCustomerCountDO, UserTaskDO inviteUserTask);
}

package com.yiling.bi.order.service;

import java.util.List;

import com.yiling.bi.order.entity.BiOrderBackupTaskDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author fucheng.bai
 * @date 2022/9/19
 */
public interface BiOrderBackupTaskService extends BaseService<BiOrderBackupTaskDO> {

    void updateBackupStatus(Long taskId, String endTime, Integer backupStatus);

    List<BiOrderBackupTaskDO> getTaskByMonth(String month);
}

package com.yiling.open.ftp.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.open.ftp.entity.FlowFtpFileTaskDO;

/**
 * @author fucheng.bai
 * @date 2023/3/21
 */
public interface FlowFtpFileTaskService extends BaseService<FlowFtpFileTaskDO> {

    void updateTaskStatusById(Integer status, Long id);
}

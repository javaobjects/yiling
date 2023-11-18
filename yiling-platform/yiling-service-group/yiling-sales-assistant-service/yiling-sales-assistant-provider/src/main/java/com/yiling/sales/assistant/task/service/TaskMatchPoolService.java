package com.yiling.sales.assistant.task.service;

import com.yiling.sales.assistant.task.entity.TaskMatchPoolDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 任务和随货同行单匹配池 服务类
 * </p>
 *
 * @author gxl
 * @date 2023-01-31
 */
public interface TaskMatchPoolService extends BaseService<TaskMatchPoolDO> {
    /**
     * 任务和随货同行单匹配定时任务
     */
    void consume();
}

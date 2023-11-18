package com.yiling.sales.assistant.task.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.task.dto.TaskTraceRegisterUserDTO;
import com.yiling.sales.assistant.task.dto.request.QueryTaskRegisterUserRequest;
import com.yiling.sales.assistant.task.entity.SaTaskRegisterUserDO;

/**
 * <p>
 * 任务和拉新人-关联表 服务类
 * </p>
 *
 * @author gxl
 * @date 2021-09-18
 */
public interface SaTaskRegisterUserService extends BaseService<SaTaskRegisterUserDO> {
    /**
     * 任务追踪-拉新人记录
     * @param queryTaskRegisterUserRequest
     * @return
     */
    Page<TaskTraceRegisterUserDTO> listTaskRegisterUserPage(QueryTaskRegisterUserRequest queryTaskRegisterUserRequest);
}

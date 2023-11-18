package com.yiling.sales.assistant.task.service;

import java.util.Date;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.task.dto.TaskTraceTerminalDTO;
import com.yiling.sales.assistant.task.dto.request.QueryTaskRegisterTerminalRequest;
import com.yiling.sales.assistant.task.entity.SaTaskRegisterTerminalDO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gxl
 * @date 2021-09-18
 */
public interface SaTaskRegisterTerminalService extends BaseService<SaTaskRegisterTerminalDO> {


    /**
     * 任务追踪-拉户类
     * @param queryTaskRegisterTerminalRequest
     * @return
     */
    Page<TaskTraceTerminalDTO> listTaskTraceTerminalPage(QueryTaskRegisterTerminalRequest queryTaskRegisterTerminalRequest);

    /**
     * 校验任务期间的新客户
     * @param createTime
     * @param auditTime
     * @param userId
     * taskStartTime
     * @return
     */
    Boolean validNewTerminal(Date createTime, Date auditTime,Long userId,Date taskStartTime);
}

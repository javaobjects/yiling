package com.yiling.sales.assistant.task.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.task.dao.SaTaskRegisterTerminalMapper;
import com.yiling.sales.assistant.task.dto.TaskTraceTerminalDTO;
import com.yiling.sales.assistant.task.dto.request.QueryTaskRegisterTerminalRequest;
import com.yiling.sales.assistant.task.entity.SaTaskRegisterTerminalDO;
import com.yiling.sales.assistant.task.service.SaTaskRegisterTerminalService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gxl
 * @date 2021-09-18
 */
@Service
public class SaTaskRegisterTerminalServiceImpl extends BaseServiceImpl<SaTaskRegisterTerminalMapper, SaTaskRegisterTerminalDO> implements SaTaskRegisterTerminalService {

    @Override
    public Page<TaskTraceTerminalDTO> listTaskTraceTerminalPage(QueryTaskRegisterTerminalRequest queryTaskRegisterTerminalRequest) {
        LambdaQueryWrapper<SaTaskRegisterTerminalDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SaTaskRegisterTerminalDO::getUserTaskId,queryTaskRegisterTerminalRequest.getUserTaskId()).orderByDesc(SaTaskRegisterTerminalDO::getId);
        Page<SaTaskRegisterTerminalDO> page = this.page(queryTaskRegisterTerminalRequest.getPage(),wrapper);
        Page<TaskTraceTerminalDTO> result = PojoUtils.map(page,TaskTraceTerminalDTO.class);
        return result;
    }

    @Override
    public Boolean validNewTerminal(Date createTime, Date auditTime, Long userId, Date taskStartTime) {
        return null;
    }
}

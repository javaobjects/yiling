package com.yiling.sales.assistant.task.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.sales.assistant.task.dao.TaskMemberRecordMapper;
import com.yiling.sales.assistant.task.dto.request.QueryTaskMemberPageRequest;
import com.yiling.sales.assistant.task.entity.TaskMemberRecordDO;
import com.yiling.sales.assistant.task.service.TaskMemberRecordService;

/**
 * <p>
 * 会员推广任务记录 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2021-12-21
 */
@Service
public class TaskMemberRecordServiceImpl extends BaseServiceImpl<TaskMemberRecordMapper, TaskMemberRecordDO> implements TaskMemberRecordService {

    @Override
    public Page<TaskMemberRecordDO> listTaskMemberPage(QueryTaskMemberPageRequest queryTaskMemberPageRequest) {
        LambdaQueryWrapper<TaskMemberRecordDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TaskMemberRecordDO::getUserTaskId,queryTaskMemberPageRequest.getUserTaskId()).orderByDesc(TaskMemberRecordDO::getId);
        Page<TaskMemberRecordDO> page = this.page(queryTaskMemberPageRequest.getPage(), wrapper);
        return page;
    }

    @Override
    public int updateRecordById(TaskMemberRecordDO memberRecordDO) {
       return this.baseMapper.updateRecordById(memberRecordDO);
    }
}

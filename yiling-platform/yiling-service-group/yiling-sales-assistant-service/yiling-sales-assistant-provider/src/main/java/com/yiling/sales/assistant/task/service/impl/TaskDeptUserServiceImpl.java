package com.yiling.sales.assistant.task.service.impl;

import org.springframework.stereotype.Service;

import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.sales.assistant.task.dao.TaskDeptUserMapper;
import com.yiling.sales.assistant.task.dto.request.DeleteDeptRequest;
import com.yiling.sales.assistant.task.entity.TaskDeptUserDO;
import com.yiling.sales.assistant.task.service.TaskDeptUserService;

/**
 * <p>
 * 任务-部门参与人员关系表 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2021-09-10
 */
@Service
public class TaskDeptUserServiceImpl extends BaseServiceImpl<TaskDeptUserMapper, TaskDeptUserDO> implements TaskDeptUserService {

    @Override
    public Boolean deleteDept(DeleteDeptRequest request) {
        int delete = this.baseMapper.deleteById(request.getTaskDeptUserId());
        if (delete > 0){
            return true;
        } else {
            return false;
        }
    }
}

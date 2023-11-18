package com.yiling.erp.client.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yiling.erp.client.service.TaskScheduleJobManager;
import com.yiling.open.erp.dto.TaskConfig;

/**
 * @author shuan
 */
@Service
public class TaskScheduleJobManagerImpl implements TaskScheduleJobManager {
    @Override
    public int deleteByPrimaryKey(String jobId) {
        return 0;
    }

    @Override
    public int insert(TaskConfig record) {
        return 0;
    }

    @Override
    public TaskConfig insertSelective(TaskConfig record) {
        return null;
    }

    @Override
    public TaskConfig selectByPrimaryKey(String jobId) {
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(TaskConfig record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(TaskConfig record) {
        return 0;
    }

    @Override
    public List<TaskConfig> getAll() {
        return null;
    }
}

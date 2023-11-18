package com.yiling.erp.client.service;

import java.util.List;

import com.yiling.open.erp.dto.TaskConfig;

/**
 * @author shuan
 */
public interface TaskScheduleJobManager {

    int deleteByPrimaryKey(String jobId);

    int insert(TaskConfig record);

    TaskConfig insertSelective(TaskConfig record);

    TaskConfig selectByPrimaryKey(String jobId);

    int updateByPrimaryKeySelective(TaskConfig record);

    int updateByPrimaryKey(TaskConfig record);

    List<TaskConfig> getAll();
}
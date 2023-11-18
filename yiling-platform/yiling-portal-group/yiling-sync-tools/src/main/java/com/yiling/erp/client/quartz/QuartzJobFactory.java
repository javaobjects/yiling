package com.yiling.erp.client.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.yiling.erp.client.util.SpringUtils;
import com.yiling.open.erp.dto.TaskConfig;

/**
 * 计划任务执行处 无状态
 *                       
 * @author shuan
 * @Filename: QuartzJobFactory.java
 * @Version: 1.0
 */
@DisallowConcurrentExecution
public class QuartzJobFactory implements Job {


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        TaskConfig scheduleJob = (TaskConfig) context.getMergedJobDataMap().get("scheduleJob");
        JobTaskService jobTaskService = (JobTaskService) SpringUtils.getBean("jobTaskService");
        jobTaskService.executeJob(scheduleJob);
    }
}
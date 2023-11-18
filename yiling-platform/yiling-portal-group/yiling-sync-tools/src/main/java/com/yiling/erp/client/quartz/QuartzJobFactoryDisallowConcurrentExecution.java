package com.yiling.erp.client.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.yiling.erp.client.util.CacheTaskUtil;
import com.yiling.erp.client.util.SpringUtils;
import com.yiling.open.erp.dto.TaskConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * 若一个方法一次执行不完下次轮转时则等待该方法执行完后才执行下一次操作
 *
 * @Filename: QuartzJobFactoryDisallowConcurrentExecution.java
 * @Version: 1.0
 * @Author: 李勇
 * @Email: yong.li@rogrand.com
 */
@Slf4j
@DisallowConcurrentExecution
public class QuartzJobFactoryDisallowConcurrentExecution implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        TaskConfig scheduleJob = (TaskConfig) context.getMergedJobDataMap().get("scheduleJob");
        Integer i = CacheTaskUtil.getInstance().getCacheData(scheduleJob.getTaskNo());
        if (i == 1) {
            log.info("上次任务还没有执行{},{}",scheduleJob.getTaskNo(),scheduleJob.getTaskName());
            return;
        }
        JobTaskService jobTaskService = (JobTaskService) SpringUtils.getBean("jobTaskService");
        jobTaskService.executeJob(scheduleJob);
    }
}
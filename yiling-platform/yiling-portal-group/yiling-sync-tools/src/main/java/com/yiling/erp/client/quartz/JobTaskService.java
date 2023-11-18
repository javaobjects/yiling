package com.yiling.erp.client.quartz;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.yiling.erp.client.service.TaskScheduleJobManager;
import com.yiling.erp.client.util.SpringUtils;
import com.yiling.open.erp.dto.TaskConfig;
import com.yiling.open.erp.dto.TaskJobRecord;
import com.yiling.open.erp.enums.ErpTopicName;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Job管理服务
 *
 * @author shuan
 * @Filename: JobTaskService.java
 * @Version: 1.0
 */
@Slf4j
@Service("jobTaskService")
public class JobTaskService {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private TaskScheduleJobManager taskScheduleJobManager;

    /**
     * 从数据库中取 区别于getAllJob
     *
     * @return
     */
    public List<TaskConfig> getAllTask() {
        return taskScheduleJobManager.getAll();
    }

    /**
     * 添加到数据库中 区别于addJob
     */
    public TaskConfig addTask(TaskConfig job) {
        return taskScheduleJobManager.insertSelective(job);
    }

    /**
     * 从数据库中查询job
     */
    public TaskConfig getTaskById(String jobId) {
        return taskScheduleJobManager.selectByPrimaryKey(jobId);
    }

    /**
     * 从数据库中更新job
     *
     * @param taskConfig
     * @return
     */
    public int updateTaskById(TaskConfig taskConfig) {
        return taskScheduleJobManager.updateByPrimaryKeySelective(taskConfig);
    }

    /**
     * 从数据库中删除job
     *
     * @param taskNo
     * @return
     */
    public int deleteTaskById(String taskNo) {
        return taskScheduleJobManager.deleteByPrimaryKey(taskNo);
    }

    /**
     * 更改任务状态
     *
     * @throws SchedulerException
     */
    public void changeStatus(String taskNo, String cmd) throws SchedulerException {
        TaskConfig job = getTaskById(taskNo);
        if (job == null) {
            return;
        }
        if ("stop".equals(cmd)) {
            deleteJob(job);
            job.setTaskStatus(TaskConfig.STATUS_NOT_RUNNING);
        } else if ("start".equals(cmd)) {
            job.setTaskStatus(TaskConfig.STATUS_RUNNING);
            addJob(job);
        }
        taskScheduleJobManager.updateByPrimaryKeySelective(job);
    }

    /**
     * 更改任务 cron表达式
     *
     * @throws SchedulerException
     */
    public void updateCron(String taskNo, String cron) throws SchedulerException {
        TaskConfig job = getTaskById(taskNo);
        if (job == null) {
            return;
        }
        job.setTaskInterval(cron);
        if (TaskConfig.STATUS_RUNNING.equals(job.getTaskStatus())) {
            updateJobCron(job);
        }
        taskScheduleJobManager.updateByPrimaryKeySelective(job);

    }

    /**
     * 添加任务
     *
     * @param job
     * @throws SchedulerException
     */
    public void addJob(TaskConfig job) throws SchedulerException {
        if (job == null || !TaskConfig.STATUS_RUNNING.equals(job.getTaskStatus())) {
            return;
        }

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        log.debug(scheduler + "................add");
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getTaskNo(), job.getTaskNo());
        SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);

        // 不存在，创建一个
        if (null == trigger) {
            Class clazz = QuartzJobFactoryDisallowConcurrentExecution.class;

            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getTaskNo(), job.getTaskNo()).build();

            jobDetail.getJobDataMap().put("scheduleJob", job);

            if (job.getTaskNo().equals(ErpTopicName.RedisCommand.getMethod())) {
                trigger = TriggerBuilder.newTrigger().withIdentity(job.getTaskNo(), job.getTaskNo()).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(Integer.parseInt(job.getTaskInterval())).withRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY)).startNow().build();
            } else {
                trigger = TriggerBuilder.newTrigger().withIdentity(job.getTaskNo(), job.getTaskNo()).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(this.getCycle(job)).withRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY)).startNow().build();
            }
            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            if (job.getTaskNo().equals(ErpTopicName.RedisCommand.getMethod())) {
                trigger = TriggerBuilder.newTrigger().withIdentity(job.getTaskNo(), job.getTaskNo()).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(Integer.parseInt(job.getTaskInterval())).withRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY)).startNow().build();
            } else {
                trigger = TriggerBuilder.newTrigger().withIdentity(job.getTaskNo(), job.getTaskNo()).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(this.getCycle(job)).withRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY)).startNow().build();
            }
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }

    public void executeJob(TaskConfig taskConfig) {
        log.debug("[" + taskConfig.getTaskName() + "]----------开始执行---------");
        TaskJobRecord record = new TaskJobRecord();
        record.setJobName(taskConfig.getTaskNo());
        record.setCronExpression(taskConfig.getTaskInterval());
        record.setSpringId(taskConfig.getSpringId());
        record.setMethodName(taskConfig.getMethodName());
        record.setStartTime(new Date());
        long begin = System.currentTimeMillis();
        int result = 0;
        try {
            Object object = null;
            Class clazz = null;
            if (StringUtils.isNotBlank(taskConfig.getSpringId())) {
                object = SpringUtils.getBean(taskConfig.getSpringId());
            }
            if (object != null) {
                clazz = object.getClass();
                Method method = null;
                method = clazz.getDeclaredMethod(taskConfig.getMethodName());
                if (method != null) {
                    method.invoke(object);
                    result = 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            record.setErrorMsg(e.toString());
            log.error("[JobTaskService][executeJob]:任务名称[" + taskConfig.getTaskName() + "]----------执行失败！", e);
        }

        if (result == 1) {
            log.debug("任务名称 = [" + taskConfig.getTaskName() + "]----------执行成功，本次任务执行时间为：" + (System.currentTimeMillis() - begin) + "毫秒");
        }

        //        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        //        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        //        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            record.setElapsedTime(System.currentTimeMillis() - begin);
            record.setExecuteResult(result);
            record.setLogTime(new Date());
            //            taskScheduleJobManager.insertRecord(record);
            //            transactionManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            //            transactionManager.rollback(status);
            log.error("[JobTaskService][executeJob]:", e);
        }
    }

    /**
     * 获取所有计划中的任务列表
     *
     * @return
     * @throws SchedulerException
     */
    public List<TaskConfig> getAllJob() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<TaskConfig> jobList = new ArrayList<TaskConfig>();
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                TaskConfig job = new TaskConfig();
                job.setTaskNo(jobKey.getName());
                //                job.setDescription("触发器:" + trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setTaskStatus(triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.setTaskInterval(cronExpression);
                }
                jobList.add(job);
            }
        }
        return jobList;
    }

    /**
     * 所有正在运行的job
     *
     * @return
     * @throws SchedulerException
     */
    public List<TaskConfig> getRunningJob() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        List<TaskConfig> jobList = new ArrayList<TaskConfig>(executingJobs.size());
        for (JobExecutionContext executingJob : executingJobs) {
            TaskConfig job = new TaskConfig();
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            job.setTaskNo(jobKey.getName());
            //            job.setJobGroup(jobKey.getGroup());
            //            job.setDescription("触发器:" + trigger.getKey());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            job.setTaskStatus(triggerState.name());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                job.setTaskInterval(cronExpression);
            }
            jobList.add(job);
        }
        return jobList;
    }

    /**
     * 获取任务的运行状态
     *
     * @param taskConfig
     * @throws SchedulerException
     */
    public Trigger.TriggerState getTriggerState(TaskConfig taskConfig) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = new JobKey(taskConfig.getTaskNo(), taskConfig.getTaskNo());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobDetail.getKey());
        if (triggers != null && triggers.size() > 0) {
            for (Trigger trigger : triggers) {
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                return triggerState;
            }
        }
        return null;
    }

    /**
     * 暂停一个job
     *
     * @param taskConfig
     * @throws SchedulerException
     */
    public void pauseJob(TaskConfig taskConfig) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(taskConfig.getTaskNo(), taskConfig.getTaskNo());
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复一个job
     *
     * @param taskConfig
     * @throws SchedulerException
     */
    public void resumeJob(TaskConfig taskConfig) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(taskConfig.getTaskNo(), taskConfig.getTaskNo());
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除一个job
     *
     * @param taskConfig
     * @throws SchedulerException
     */
    public void deleteJob(TaskConfig taskConfig) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(taskConfig.getTaskNo(), taskConfig.getTaskNo());
        scheduler.deleteJob(jobKey);
    }

    /**
     * 立即执行job
     *
     * @param taskConfig
     * @throws SchedulerException
     */
    public void runAJobNow(TaskConfig taskConfig) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(taskConfig.getTaskNo(), taskConfig.getTaskNo());
        scheduler.triggerJob(jobKey);
    }

    /**
     * 更新job时间表达式
     *
     * @param taskConfig
     * @throws SchedulerException
     */
    public void updateJobCron(TaskConfig taskConfig) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        TriggerKey triggerKey = TriggerKey.triggerKey(taskConfig.getTaskNo(), taskConfig.getTaskNo());

        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(taskConfig.getTaskInterval());

        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

        scheduler.rescheduleJob(triggerKey, trigger);
    }

    /**
     * 更新任务
     *
     * @param taskConfig
     * @throws SchedulerException
     */
    public void updateJob(TaskConfig taskConfig) throws SchedulerException {
        // 更新数据库
        taskScheduleJobManager.updateByPrimaryKeySelective(taskConfig);

        // 更新任务列表中的任务
        taskConfig = getTaskById(taskConfig.getTaskNo());
        updateJobCron(taskConfig);
    }

    //    public Date getStartDateTime(TaskConfig taskConfig) {
    //        // TaskInterval:21 26 14 * * ?
    //        if (taskConfig.getTaskNo().equals(ErpTopicName.ErpPurchaseFlow.getMethod()) || taskConfig.getTaskNo().equals(ErpTopicName.ErpSaleFlow.getMethod())) {
    //            String[] times = taskConfig.getTaskInterval().split(" ");
    //            return DateUtil.parse(DateUtil.format(new Date(), "yyyy-MM-dd") + " " + times[2] + ":" + times[1] + ":" + times[0], "yyyy-MM-dd HH:mm:ss");
    //        } else {
    //            return new Date();
    //        }
    //    }

    public Integer getCycle(TaskConfig taskConfig) {
        if (StrUtil.isNotEmpty(taskConfig.getTaskInterval()) && taskConfig.getTaskInterval().contains("-")) {
            return 60 * 24;
        } else {
            return Integer.parseInt(taskConfig.getTaskInterval());
        }
    }

    @PreDestroy
    public void destroy() {
        System.err.println("执行InitAndDestroySeqBean: destroy-method");
    }
}

package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.sales.assistant.task.api.TaskApi;

import lombok.extern.slf4j.Slf4j;

/**
 * 销售助手任务启动定时
 * @author: gxl
 * @date: 2022/2/8
 */
@Component
@Slf4j
public class StartTaskJobHandler {
    @DubboReference
    TaskApi taskApi;
    @JobLog
    @XxlJob("startTaskJobHandler")
    public ReturnT<String> publishTask(String param) throws Exception {
        log.info("开始执行发布任务");
        taskApi.publishTask();
        log.info("结束执行发布任务");
        return ReturnT.SUCCESS;
    }
}
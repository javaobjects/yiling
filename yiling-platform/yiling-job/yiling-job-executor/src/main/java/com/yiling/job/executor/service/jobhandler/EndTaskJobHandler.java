package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.sales.assistant.task.api.TaskApi;

import lombok.extern.slf4j.Slf4j;

/**
 * 销售助手任务结束
 * @author: ray
 * @date: 2022/2/8
 */
@Component
@Slf4j
public class EndTaskJobHandler {
    @DubboReference
    TaskApi taskApi;

    @JobLog
    @XxlJob("endTaskJobHandler")
    public ReturnT<String> endTask(String param) throws Exception {
        log.info("开始执行结束任务");
        taskApi.endTask();
        log.info("结束执行结束任务");
        return ReturnT.SUCCESS;
    }
}
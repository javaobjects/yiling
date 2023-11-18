package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.sales.assistant.task.api.TaskApi;

import lombok.extern.slf4j.Slf4j;

/**
 * 随货同行单匹配流向定时任务
 * @author: gxl
 * @date: 2023/2/2
 */
@Component
@Slf4j
public class AccompanyingBillFlowMatchJobHandler {

    @DubboReference
    TaskApi taskApi;

    @JobLog
    @XxlJob("accompanyingBillFlowMatchJobHandler")
    public ReturnT<String> billFlowMatch(String param) throws Exception {
        taskApi.billFlowMatchTimer();
        return ReturnT.SUCCESS;
    }
}
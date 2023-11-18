package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.sjms.gb.api.GbStatisticApi;;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wei.wang
 */
@Slf4j
@Component
public class GbStatisticProgramHandler {
    @DubboReference(async = true)
    GbStatisticApi gbStatisticApi;

    @JobLog
    @XxlJob("gbStatisticProgramHandler")
    public ReturnT<String> gbStatisticProgramHandler(String param) throws Exception {
        log.info("任务开始:统计每日团购提报和取消任务");
        gbStatisticApi.gBStatisticProgram();
        log.info("任务结束:统计每日团购提报和取消任务");
        return ReturnT.SUCCESS;
    }
}

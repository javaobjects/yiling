package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.dataflow.order.api.FlowSaleApi;
import com.yiling.job.executor.log.JobLog;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/6/29
 */
@Component
@Slf4j
public class FlowSaleReportSyncJobHandler {

    @DubboReference(async = true)
    private FlowSaleApi flowSaleApi;

    @JobLog
    @XxlJob("flowSaleReportSyncJobHandler")
    public ReturnT<String> flowSaleReportSyncByEnterpriseTagJobHandler(String param) throws Exception {
        log.info("任务开始：销售流向根据商业标签同步到返利任务执行开始");
        flowSaleApi.flowSaleReportSyncByEnterpriseTag();
        log.info("任务结束：销售流向根据商业标签同步到返利任务执行结束");
        return ReturnT.SUCCESS;
    }

}

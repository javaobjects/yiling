package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.dataflow.order.api.FlowPurchaseApi;
import com.yiling.job.executor.log.JobLog;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/10/26
 */
@Component
@Slf4j
public class FlowPurchaseQuantityJobHandler {

    @DubboReference(async = true)
    private FlowPurchaseApi flowPurchaseApi;

    @JobLog
    @XxlJob("flowPurchaseTotalQuantityJobHandler")
    public ReturnT<String> flowPurchaseTotalQuantityJobHandler(String param) throws Exception {
        log.info("任务开始：采购流向采购数量统计任务执行");
        long start = System.currentTimeMillis();
        flowPurchaseApi.statisticsFlowPurchaseTotalQuantity();
        log.info("任务结束：采购流向采购数量统计任务执行。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

}

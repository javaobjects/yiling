package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.dataflow.order.api.FlowGoodsBatchApi;
import com.yiling.dataflow.order.api.FlowPurchaseApi;
import com.yiling.dataflow.order.api.FlowSaleApi;
import com.yiling.job.executor.log.JobLog;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2022/7/21
 */
@Component
@Slf4j
public class FlowSpecSyncHandler {

    @DubboReference
    private FlowPurchaseApi flowPurchaseApi;

    @DubboReference
    private FlowSaleApi flowSaleApi;

    @DubboReference
    private FlowGoodsBatchApi flowGoodsBatchApi;


    @JobLog
    @XxlJob("syncFlowSpecHandler")
    public ReturnT<String> syncFlowSpec(String param) throws Exception {
        flowPurchaseApi.syncFlowPurchaseSpec();
        flowSaleApi.syncFlowSaleSpec();
        flowGoodsBatchApi.syncFlowGoodsBatchSpec();
        return ReturnT.SUCCESS;
    }
}

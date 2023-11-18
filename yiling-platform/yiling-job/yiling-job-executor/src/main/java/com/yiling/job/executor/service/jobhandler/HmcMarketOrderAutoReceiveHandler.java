package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.hmc.order.api.MarketOrderApi;
import com.yiling.job.executor.log.JobLog;

import lombok.extern.slf4j.Slf4j;

/**
 * 市场订单自动收货
 *
 * @author: benben.jia
 * @date: 2023/03/08
 */
@Component
@Slf4j
public class HmcMarketOrderAutoReceiveHandler {

    @DubboReference(async = true)
    MarketOrderApi marketOrderApi;

    @JobLog
    @XxlJob("marketOrderAutoReceiveHandler")
    public ReturnT<String> marketOrderAutoReceiveHandler(String param) throws Exception {
        log.info("任务开始：hmc市场订单10天自动收货");
        marketOrderApi.marketOrderAutoReceive(10);
        DubboUtils.quickAsyncCall("marketOrderApi","marketOrderAutoReceive");
        log.info("任务结束：hmc市场订单10天自动收货");
        return ReturnT.SUCCESS;
    }
}

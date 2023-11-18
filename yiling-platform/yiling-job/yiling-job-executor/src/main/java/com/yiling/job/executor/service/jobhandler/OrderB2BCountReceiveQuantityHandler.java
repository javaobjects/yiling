package com.yiling.job.executor.service.jobhandler;

import java.util.ArrayList;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.job.executor.log.JobLog;
import com.yiling.mall.order.api.OrderProcessApi;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author wei.wang
 * @version V1.0
 * @Package com.yiling.job.executor.service.jobhandler
 * @date: 2021/11/25
 */
@Component
@Slf4j
public class OrderB2BCountReceiveQuantityHandler {

    @DubboReference(async = true)
    OrderProcessApi orderProcessApi;

    @JobLog
    @XxlJob("orderB2BCountReceiveQuantityHandler")
    public ReturnT<String> orderB2BCountReceiveQuantityHandler(String param) throws Exception {
        log.info("任务开始：统计B2B收货商品数量");
        orderProcessApi.countReceiveOrder(new ArrayList<Integer>(){{add(2);}});

        log.info("任务结束：统计B2B收货商品数量");
        return ReturnT.SUCCESS;
    }
}

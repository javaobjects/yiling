package com.yiling.job.executor.service.jobhandler;

import java.util.Calendar;
import java.util.Date;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.job.executor.log.JobLog;
import com.yiling.mall.strategy.api.StrategyApi;
import com.yiling.marketing.strategy.api.StrategyActivityApi;

import lombok.extern.slf4j.Slf4j;

/**
 * 策略满赠参与定时任务
 *
 * @author: yong.zhang
 * @date: 2022/9/6
 */
@Component
@Slf4j
public class StrategyActivityAutoJobHandler {

    @DubboReference(timeout = 1000 * 80)
    StrategyActivityApi strategyActivityApi;

    @DubboReference(timeout = 1000 * 80)
    StrategyApi strategyApi;

    @JobLog
    @XxlJob("strategyActivityAutoJobHandler")
    public ReturnT<String> strategyActivityAutoJobHandler(String param) throws Exception {
        log.info("任务开始：自动发放策略满赠赠品");
        strategyActivityApi.strategyActivityAutoJobHandler();
        DubboUtils.quickAsyncCall("strategyActivityApi", "strategyActivityAutoJobHandler");
        log.info("任务结束：自动发放策略满赠赠品");
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("strategyEndAutoJobHandler")
    public ReturnT<String> strategyEndAutoJobHandler(String param) throws Exception {
        log.info("任务开始：策略满赠结束后自动发放策略满赠赠品");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startTime = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date stopTime = calendar.getTime();
        strategyApi.strategyEndAutoJobHandler(startTime, stopTime);
        DubboUtils.quickAsyncCall("strategyApi", "strategyEndAutoJobHandler");
        log.info("任务结束：策略满赠结束后自动发放策略满赠赠品");
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("strategyMemberAutoJobHandler")
    public ReturnT<String> strategyMemberAutoJobHandler(String param) throws Exception {
        log.info("任务开始：续费会员自动发放策略满赠赠品");
        strategyActivityApi.strategyMemberAutoJobHandler();
        DubboUtils.quickAsyncCall("strategyActivityApi", "strategyMemberAutoJobHandler");
        log.info("任务结束：续费会员自动发放策略满赠赠品");
        return ReturnT.SUCCESS;
    }
}

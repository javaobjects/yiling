package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityTimesApi;

import lombok.extern.slf4j.Slf4j;

/**
 * C端抽端奖次数清零任务
 *
 * @author: lun.yu
 * @date: 2022-09-27
 */
@Component
@Slf4j
public class LotteryTimesClearHandler {

    @DubboReference
    LotteryActivityTimesApi lotteryActivityTimesApi;

    /**
     * 每日0点C端抽端奖次数清零
     *
     * @param param
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("clearUserLotteryTimes")
    public ReturnT<String> clearUserLotteryTimes(String param) throws Exception {
        log.info("任务开始：C抽端奖次数清零");
        lotteryActivityTimesApi.clearUserLotteryTimes();
        log.info("任务结束：C抽端奖次数清零");
        return ReturnT.SUCCESS;
    }
}

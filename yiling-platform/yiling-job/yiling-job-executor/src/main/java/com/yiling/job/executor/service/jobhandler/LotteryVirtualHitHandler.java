package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityJoinDetailApi;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityTimesApi;

import lombok.extern.slf4j.Slf4j;

/**
 * 抽奖活动-生成虚拟中奖名单任务
 *
 * @author: lun.yu
 * @date: 2022-10-31
 */
@Component
@Slf4j
public class LotteryVirtualHitHandler {

    @DubboReference
    LotteryActivityJoinDetailApi lotteryActivityJoinDetailApi;

    /**
     * 生成虚拟中奖信息（定时任务每30分钟执行）
     *
     * @param param
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("generateVirtualHit")
    public ReturnT<String> generateVirtualHit(String param) throws Exception {
        log.info("任务开始：生成虚拟中奖名单任务");
        lotteryActivityJoinDetailApi.generateVirtualHit();
        log.info("任务结束：生成虚拟中奖名单任务");
        return ReturnT.SUCCESS;
    }
}

package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityApi;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityTimesApi;

import lombok.extern.slf4j.Slf4j;

/**
 * C端抽奖活动随机生成中奖名单任务
 *
 * @author: lun.yu
 * @date: 2022-09-27
 */
@Component
@Slf4j
public class LotteryGenerateHitHandler {

    @DubboReference
    LotteryActivityApi lotteryActivityApi;

    /**
     * 每小时C端抽奖活动随机生成中奖名单
     *
     * @param param
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("generateHitTask")
    public ReturnT<String> generateHitTask(String param) throws Exception {
        log.info("任务开始：每小时C端抽奖活动随机生成中奖名单");
        lotteryActivityApi.generateHitTask();
        log.info("任务结束：每小时C端抽奖活动随机生成中奖名单");
        return ReturnT.SUCCESS;
    }
}

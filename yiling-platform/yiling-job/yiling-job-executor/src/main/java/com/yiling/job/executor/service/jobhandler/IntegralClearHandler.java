package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.user.integral.api.UserIntegralApi;
import com.yiling.user.integral.enums.IntegralRulePlatformEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * 积分清零任务
 *
 * @author: lun.yu
 * @date: 2023-02-06
 */
@Component
@Slf4j
public class IntegralClearHandler {

    @DubboReference(async = true)
    UserIntegralApi userIntegralApi;

    /**
     * 每年1月1日零时，清零上上年度12月1日至上年度11月30日的积分
     *
     * @param param
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("clearB2bIntegral")
    public ReturnT<String> clearB2bIntegral(String param) throws Exception {
        log.info("任务开始：B端积分清零");
        userIntegralApi.clearIntegral(IntegralRulePlatformEnum.B2B.getCode());
        log.info("任务结束：B端积分清零");
        return ReturnT.SUCCESS;
    }

    /**
     * 指定时间清零定向赠送积分
     *
     * @param param
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("cleanDirectionalGiveIntegral")
    public ReturnT<String> cleanDirectionalGiveIntegral(String param) throws Exception {
        log.info("任务开始：B端清零定向赠送积分");
        userIntegralApi.cleanDirectionalGiveIntegral();
        log.info("任务结束：B端清零定向赠送积分");
        return ReturnT.SUCCESS;
    }

}

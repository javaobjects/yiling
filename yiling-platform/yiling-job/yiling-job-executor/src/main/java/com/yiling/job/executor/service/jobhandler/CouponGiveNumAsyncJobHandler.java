package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;

import lombok.extern.slf4j.Slf4j;

/**
 * 会员每月自动发券
 * 每月1日凌晨0点执行
 *
 * @author: shixing.sun
 * @date: 2023/01/09
 */
@Slf4j
@Component
public class CouponGiveNumAsyncJobHandler {

    @DubboReference
    CouponActivityApi couponApi;

    @JobLog
    @XxlJob("CouponGiveNumAsyncJobHandler")
    public ReturnT<String> couponGiveNumAsyncJobHandler(String param) throws Exception {
        log.info("CouponGiveNumAsyncJobHandler: 任务开始, 参数{}",param);
        couponApi.SyncCouponGiveNum();
        log.info("couponAutoGiveMonthByMemberHandler: 任务结束");
        return ReturnT.SUCCESS;
    }

}

package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.mall.coupon.api.CouponApi;

import lombok.extern.slf4j.Slf4j;

/**
 * 会员每月自动发券
 * 每月1日凌晨0点执行
 *
 * @author: houjie.sun
 * @date: 2021/12/17
 */
@Slf4j
@Component
public class CouponAutoGiveByMemberJobHandler {

    @DubboReference(async = true)
    CouponApi couponApi;

    @JobLog
    @XxlJob("couponAutoGiveMonthByMemberHandler")
    public ReturnT<String> couponAutoGiveMonthByMemberHandler(String param) throws Exception {
        // 0 0 0 1 * ? * 每月1号
        // 0 0/5 * * * ? 每5分钟
        // 0 0 0/1 * * ? 每1小时
        log.info("couponAutoGiveMonthByMemberHandler: 任务开始, 参数{}",param);
        couponApi.memberAutoGiveByMonth();
        log.info("couponAutoGiveMonthByMemberHandler: 任务结束");
        return ReturnT.SUCCESS;
    }

}

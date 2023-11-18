package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.mall.member.api.MemberApi;

import lombok.extern.slf4j.Slf4j;

/**
 * B2B会员到期续费提醒
 *
 * @author: lun.yu
 * @date: 2021/12/13
 */
@Component
@Slf4j
public class MemberExpirationReminderHandler {

    @DubboReference
    MemberApi memberApi;

    /**
     * 会员到期提醒：每天上午10点进行检查是否存在到期需要提醒的会员用户
     * @param param
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("memberExpirationReminderHandler")
    public ReturnT<String> memberExpirationReminderHandler(String param) throws Exception {
        log.info("任务开始：B2B会员到期续费提醒");
        memberApi.memberExpirationReminderHandler();
        log.info("任务结束：B2B会员到期续费提醒");
        return ReturnT.SUCCESS;
    }
}

package com.yiling.job.executor.service.jobhandler;

import com.yiling.hmc.remind.api.MedsRemindApi;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.hmc.order.api.OrderApi;
import com.yiling.job.executor.log.JobLog;

import lombok.extern.slf4j.Slf4j;

/**
 * 取药提醒定时任务
 *
 * @author: yong.zhang
 * @date: 2022/4/26
 */
@Component
@Slf4j
public class HmcMedicineRemindJobHandler {

    @DubboReference(timeout = 1000 * 80)
    OrderApi orderApi;

    @DubboReference(timeout = 1000 * 80)
    MedsRemindApi medsRemindApi;

    @JobLog
    @XxlJob("hmcMedicineRemindJobHandler")
    public ReturnT<String> hmcMedicineRemindJobHandler(String param) throws Exception {
        log.info("任务开始：取药提醒");
        orderApi.sendMedicineRemind();
        DubboUtils.quickAsyncCall("orderApi", "sendMedicineRemind");
        log.info("任务结束：取药提醒");
        return ReturnT.SUCCESS;
    }

    /**
     * 用药管理 - 生成每日用药提醒任务
     *
     * @param param
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("medsRemindJobHandler")
    public ReturnT<String> medsRemindJobHandler(String param) throws Exception {
        log.info("[medsRemindJobHandler]生成每日用药提醒任务开始");
        medsRemindApi.generateDailyMedsRemindTask();
        DubboUtils.quickAsyncCall("medsRemindApi", "generateDailyMedsRemindTask");
        log.info("[medsRemindJobHandler]生成每日用药提醒任务结束");
        return ReturnT.SUCCESS;
    }

    /**
     * 中秋节推送微信模板消息job
     *
     * @param param
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("midAutumnFestivalPushWxTemplateMsg")
    public ReturnT<String> midAutumnFestivalPushWxTemplateMsg(String param) throws Exception {
        log.info("[midAutumnFestivalPushWxTemplateMsg]中秋节活动-推送微信模板消息job开始");
        medsRemindApi.midAutumnFestivalPushWxTemplateMsg();
        DubboUtils.quickAsyncCall("medsRemindApi", "midAutumnFestivalPushWxTemplateMsg");
        log.info("[midAutumnFestivalPushWxTemplateMsg]中秋节活动-推送微信模板消息job结束");
        return ReturnT.SUCCESS;
    }

    /**
     * 父亲节推送微信模板消息job
     *
     * @param param
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("fatherFestivalPushWxTemplateMsg")
    public ReturnT<String> fatherFestivalPushWxTemplateMsg(String param) throws Exception {
        log.info("[fatherFestivalPushWxTemplateMsg]父亲节活动-推送微信模板消息job开始");
        medsRemindApi.fatherFestivalPushWxTemplateMsg();
        DubboUtils.quickAsyncCall("medsRemindApi", "fatherFestivalPushWxTemplateMsg");
        log.info("[fatherFestivalPushWxTemplateMsg]父亲节活动-推送微信模板消息job结束");
        return ReturnT.SUCCESS;
    }


}

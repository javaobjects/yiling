package com.yiling.job.executor.service.jobhandler;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.request.PromotionGoodsGiftReturnRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2021/11/16
 */
@Component
@Slf4j
public class PromotionGiftReturnHandler {
    @DubboReference
    PromotionActivityApi activityApi;

    @JobLog
    @XxlJob("promotionGiftReturnHandler")
    public ReturnT<String> promotionGiftReturn(String param) throws Exception {
        PromotionGoodsGiftReturnRequest request = new PromotionGoodsGiftReturnRequest();
        request.setStartTime(DateUtils.addMonths(new Date(), -3));
        request.setEndTime(new Date());
        request.setOpUserId(1L);
        request.setOpTime(new Date());
        log.info("任务开始:营销满赠活动结束后退回赠品，请求参数为:[{}]", request);
        activityApi.returnPromotionGoodsGift(request);
        log.info("任务结束:营销满赠活动结束后退回赠品");
        return ReturnT.SUCCESS;
    }
}

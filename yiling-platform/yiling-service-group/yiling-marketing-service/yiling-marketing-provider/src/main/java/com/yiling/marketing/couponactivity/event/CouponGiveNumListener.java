package com.yiling.marketing.couponactivity.event;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.yiling.marketing.couponactivity.service.CouponActivityService;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveRecordDO;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单创建完成逻辑处理
 *
 * @author shixing.sun
 * @version V1.0
 * @Package com.yiling.marketing.couponactivity.event
 * @date: 2023/1/3
 */
@Component
@Slf4j
public class CouponGiveNumListener {

    @Autowired
    private CouponActivityService couponActivityService;


    /**
     * 更新优惠券信息
     *
     * @param updateCouponGiveNumEvent
     */
    @Async
    @EventListener
    @Order(1)
    public void reducePaymentDaysQuota(UpdateCouponGiveNumEvent updateCouponGiveNumEvent) {
        log.info("..updateCouponGiveNumEvent...{}", JSON.toJSON(updateCouponGiveNumEvent));
        if (CollectionUtil.isEmpty(updateCouponGiveNumEvent.getCouponActivityAutoGiveRecordDO())) {
            return;
        }
        List<CouponActivityAutoGiveRecordDO> newList = new ArrayList<>();
        updateCouponGiveNumEvent.getCouponActivityAutoGiveRecordDO().parallelStream().collect(Collectors.groupingBy(CouponActivityAutoGiveRecordDO::getCouponActivityId, Collectors.toList())).forEach((materialId, list) -> list.stream().reduce((a, b) -> new CouponActivityAutoGiveRecordDO().setCouponActivityId(a.getCouponActivityId()).setGiveNum(a.getGiveNum() + b.getGiveNum())).ifPresent(newList::add));
        for (CouponActivityAutoGiveRecordDO orderDto : newList) {
            couponActivityService.updateHasGiveNum(orderDto.getCouponActivityId(), orderDto.getGiveNum());
        }
    }
}

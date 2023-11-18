package com.yiling.mall.coupon.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.mall.coupon.api.CouponApi;
import com.yiling.mall.coupon.handler.CouponAutoGiveByMemberMonthHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/1/5
 */
@Slf4j
@DubboService
public class CouponApiImpl implements CouponApi {

    @Autowired
    private CouponAutoGiveByMemberMonthHandler memberMonthHandler;

    @Override
    public Boolean memberAutoGiveByMonth() {
        return memberMonthHandler.memberAutoGiveByMonth();
    }
}

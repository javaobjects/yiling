package com.yiling.mall.coupon;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.mall.BaseTest;
import com.yiling.mall.coupon.bo.CouponActivityAutoGiveContext;
import com.yiling.mall.coupon.handler.CouponAutoGiveByMemberHandler;
import com.yiling.mall.coupon.handler.CouponAutoGiveByMemberMonthHandler;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveDetailDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.dto.CurrentMemberDTO;
import com.yiling.user.member.dto.request.CurrentMemberForMarketingDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: fan.shen
 * @date: 2021/12/30
 */
@Slf4j
public class CouponAutoGiveByMemberTest extends BaseTest {

    @DubboReference
    EnterpriseApi                         enterpriseApi;

    @DubboReference
    MemberApi                             memberApi;

    @Autowired
    private CouponAutoGiveByMemberMonthHandler autoGiveByMemberMonthHandler;

    @Autowired
    private CouponAutoGiveByMemberHandler couponAutoGiveByMemberHandler;


    @Test
    public void test() {

        autoGiveByMemberMonthHandler.memberAutoGiveByMonth();

    }

    @Test
    public void couponAutoGiveByMember() {

        Long eid = 275L;

        EnterpriseDTO enterprise = enterpriseApi.getById(eid);

        CurrentMemberForMarketingDTO member = memberApi.getCurrentMemberForMarketing(eid);

        CouponActivityAutoGiveContext context = CouponActivityAutoGiveContext.builder().build();
        context.setCurrentMemberDTO(member);
        context.setEnterpriseDTO(enterprise);

        List<CouponActivityAutoGiveDetailDTO> autoGiveDetailList = couponAutoGiveByMemberHandler.checkMemberGiveRules(context);

        boolean isSuccess = couponAutoGiveByMemberHandler.memberHandler(enterprise, autoGiveDetailList);

    }
}

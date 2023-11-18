package com.yiling.mall.coupon.bo;

import java.util.List;

import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveDetailDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveEnterpriseLimitDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.dto.CurrentMemberDTO;
import com.yiling.user.member.dto.request.CurrentMemberForMarketingDTO;

import lombok.Builder;
import lombok.Data;

/**
 * 自动发放优惠券上下文类
 * @Description
 * @Author fan.shen
 * @Date 2022/1/7 17:25:23
 */
@Data
@Builder
public class CouponActivityAutoGiveContext {

    /**
     * 企业信息
     */
    private EnterpriseDTO enterpriseDTO;

    /**
     * 会员信息
     */
    private CurrentMemberForMarketingDTO currentMemberDTO;

    /**
     * 待校验活动信息
     */
    private CouponActivityAutoGiveDetailDTO waitCheckAutoGiveDTO;

    /**
     * 部分会员
     */
    List<CouponActivityAutoGiveEnterpriseLimitDTO> enterpriseLimitList;



}

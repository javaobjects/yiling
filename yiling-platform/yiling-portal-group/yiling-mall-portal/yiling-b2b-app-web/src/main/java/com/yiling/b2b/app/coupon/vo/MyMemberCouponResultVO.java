package com.yiling.b2b.app.coupon.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/17
 */
@Data
@Accessors(chain = true)
public class MyMemberCouponResultVO extends BaseVO {
    /**
     * 可用优惠券
     */
    private List<MyMemberCouponVO> valibaleMemberCoupon;

    /**
     * 不可用优惠券
     */
    private List<MyMemberCouponVO> notValiableMemberCoupon;
}

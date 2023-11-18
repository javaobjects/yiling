package com.yiling.marketing.coupon.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/11
 */
@Data
@Accessors(chain = true)
public class UseMemberCouponRequest extends BaseRequest {

    /**
     * 卡包优惠券id
     */
    private Long id;

    /**
     * 当前用户id
     */
    private Long currentEid;

    /**
     * 当前用户id
     */
    private Long currentUserId;

    /**
     * 当前会员id
     */
    private Long memberId;
}

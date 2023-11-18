package com.yiling.marketing.couponactivity.dto;

import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * B2B-优惠券详情DTO
 *
 * @author: houjie.sun
 * @date: 2021/10/27
 */
@Data
@Accessors(chain = true)
public class CouponActivityForMemberResultDTO extends BaseDTO {
    /**
     * 可用会员优惠券
     */
    private List<CouponActivityForMemberDTO> valibaleMemberCoupon;
    /**
     * 不可用会员优惠券
     */
    private List<CouponActivityForMemberDTO> notValiableMemberCoupon;
}

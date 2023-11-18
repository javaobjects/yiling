package com.yiling.marketing.couponactivity.dto;

import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityCanAndOwnDTO extends BaseDTO {

    /**
     * 可领取优惠券
     */
    private List<CouponActivityHasGetDTO> canList;

    /**
     * 已领取优惠券
     */
    private List<CouponActivityHasGetDTO> ownList;

}

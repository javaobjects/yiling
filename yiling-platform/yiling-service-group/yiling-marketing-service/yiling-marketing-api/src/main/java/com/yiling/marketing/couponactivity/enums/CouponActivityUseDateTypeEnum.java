package com.yiling.marketing.couponactivity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 优惠券活动-用券时间类型
 *
 * @author: houjie.sun
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum CouponActivityUseDateTypeEnum {

    /**
     * 1-固定时间
     */
    FIXED(1, "固定时间"),
    /**
     * 2-发放/领取后XX天失效
     */
    AFTER_GIVE(2, "发放/领取后XX天失效"),
    ;

    private Integer code;
    private String  name;

    public static CouponActivityUseDateTypeEnum getByCode(Integer code) {
        for (CouponActivityUseDateTypeEnum e : CouponActivityUseDateTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}

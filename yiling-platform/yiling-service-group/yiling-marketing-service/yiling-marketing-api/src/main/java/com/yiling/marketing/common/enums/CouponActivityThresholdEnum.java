package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 优惠券活动类型（1-支付金额满额）
 * @author: houjie.sun
 * @date: 2021/10/29
 */
@Getter
@AllArgsConstructor
public enum CouponActivityThresholdEnum {

    // 1-支付金额满额
    PAYMENT_SATISFY_AMOUNT (1, "支付金额满额"),
    ;

    private Integer code;
    private String name;

    public static CouponActivityThresholdEnum getByCode(Integer code) {
        for (CouponActivityThresholdEnum e : CouponActivityThresholdEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

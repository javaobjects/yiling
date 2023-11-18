package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 优惠券类型（1-满减券；2-满折券）
 * @author: houjie.sun
 * @date: 2021/10/29
 */
@Getter
@AllArgsConstructor
public enum CouponActivityTypeEnum {

    // 1-满减券
    REDUCE(1, "满减券","(满减)"),
    // 2-满折券
    DISCOUNT(2, "满折券","(满折)"),
    ;

    private Integer code;

    private String name;

    private String shortName;

    public static CouponActivityTypeEnum getByCode(Integer code) {
        for (CouponActivityTypeEnum e : CouponActivityTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

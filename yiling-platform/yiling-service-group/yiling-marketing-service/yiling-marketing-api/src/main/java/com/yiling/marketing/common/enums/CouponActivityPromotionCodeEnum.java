package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否包含推广码
 * @author: fan.shen
 * @date: 2021/12/30
 */
@Getter
@AllArgsConstructor
public enum CouponActivityPromotionCodeEnum {

    CONTAIN(1, "包含"),

    NOT_CONTAIN(2, "不包含"),

    ALL(3, "不使用"),

    ;

    private Integer code;
    private String name;

    public static CouponActivityPromotionCodeEnum getByCode(Integer code) {
        for (CouponActivityPromotionCodeEnum e : CouponActivityPromotionCodeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 可叠加促销类型
 * @author: houjie.sun
 * @date: 2021/12/07
 */
@Getter
@AllArgsConstructor
public enum CouponActivityCoexistPromotionEnum {

    // 1-满赠
    FULL_GIFT(1, "满赠"),
    ;

    private Integer code;
    private String name;

    public static CouponActivityCoexistPromotionEnum getByCode(Integer code) {
        for (CouponActivityCoexistPromotionEnum e : CouponActivityCoexistPromotionEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

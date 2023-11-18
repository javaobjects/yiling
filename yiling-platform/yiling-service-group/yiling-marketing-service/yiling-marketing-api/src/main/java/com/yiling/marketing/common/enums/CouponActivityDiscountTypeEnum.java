package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 优惠方式
 * @author: houjie.sun
 * @date: 2021/10/29
 */
@Getter
@AllArgsConstructor
public enum CouponActivityDiscountTypeEnum {

    // 1-金额满减/抵扣
    AMOUNT_REDUCE (1, "金额满减/抵扣"),
    // 2-折扣比例（98.00%）
    DISCOUNT_RATIO (2, "折扣比例"),
    ;

    private Integer code;
    private String name;

    public static CouponActivityDiscountTypeEnum getByCode(Integer code) {
        for (CouponActivityDiscountTypeEnum e : CouponActivityDiscountTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

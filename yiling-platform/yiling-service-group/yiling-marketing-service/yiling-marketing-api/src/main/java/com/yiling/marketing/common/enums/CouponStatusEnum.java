package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 使用状态
 * @author: houjie.sun
 * @date: 2021/10/27
 */
@Getter
@AllArgsConstructor
public enum CouponStatusEnum {

    /**
     * 1-正常
     */
    NORMAL_COUPON(1, "正常"),
    /**
     * 2-废弃
     */
    SCRAP_COUPON(2, "废弃"),
    ;

    private Integer code;
    private String  name;

    public static CouponStatusEnum getByCode(Integer code) {
        for (CouponStatusEnum e : CouponStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

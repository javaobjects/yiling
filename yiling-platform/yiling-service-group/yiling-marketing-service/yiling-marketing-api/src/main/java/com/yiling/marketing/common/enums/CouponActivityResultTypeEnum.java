package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2021/10/28
 */
@Getter
@AllArgsConstructor
public enum CouponActivityResultTypeEnum {

    // 1-成功
    SUCCESS(1, "成功"),
    // 2-失败
    FAIL(2, "失败"),
    ;

    private Integer code;
    private String name;

    public static CouponActivityResultTypeEnum getByCode(Integer code) {
        for (CouponActivityResultTypeEnum e : CouponActivityResultTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

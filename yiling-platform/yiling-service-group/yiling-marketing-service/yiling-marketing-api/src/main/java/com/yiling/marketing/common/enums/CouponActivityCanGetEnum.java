package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2021/10/26
 */
@Getter
@AllArgsConstructor
public enum CouponActivityCanGetEnum {

    // 1-用户可领
    CAN_GET(1, "用户可领"),
    // 2-用户不可领
    CAN_NOT_GET(2, "用户不可领"),
    ;

    private Integer code;
    private String name;

    public static CouponActivityCanGetEnum getByCode(Integer code) {
        for (CouponActivityCanGetEnum e : CouponActivityCanGetEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

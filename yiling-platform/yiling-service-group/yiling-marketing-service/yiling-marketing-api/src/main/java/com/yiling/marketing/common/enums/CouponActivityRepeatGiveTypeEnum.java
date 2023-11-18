package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2021/11/4
 */
@Getter
@AllArgsConstructor
public enum CouponActivityRepeatGiveTypeEnum {

    // 1-仅发一次
    ONE (1, "仅发一次"),
    // 2-重复发放
    MANY (2, "重复发放"),
    ;

    private Integer code;
    private String name;

    public static CouponActivityRepeatGiveTypeEnum getByCode(Integer code) {
        for (CouponActivityRepeatGiveTypeEnum e : CouponActivityRepeatGiveTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

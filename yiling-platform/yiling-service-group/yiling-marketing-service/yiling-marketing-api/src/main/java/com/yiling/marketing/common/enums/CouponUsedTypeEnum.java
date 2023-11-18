package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2021/11/11
 */
@Getter
@AllArgsConstructor
public enum CouponUsedTypeEnum {

    /**
     * 1-未使用
     */
    NOT_USED(1, "未使用"),
    /**
     * 2-已使用
     */
    USED(2, "已使用"),
    /**
     * 3-已过期
     */
    EXPIRE(3, "已过期"),
    ;

    private Integer code;
    private String  name;

    public static CouponUsedTypeEnum getByCode(Integer code) {
        for (CouponUsedTypeEnum e : CouponUsedTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

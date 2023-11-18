package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2021/11/2
 */
@Getter
@AllArgsConstructor
public enum CouponUsedStatusEnum {

    /**
     * 1-未使用
     */
    NOT_USED(1, "未使用"),
    /**
     * 2-已使用
     */
    USED(2, "已使用"),
    ;

    private Integer code;
    private String  name;

    public static CouponUsedStatusEnum getByCode(Integer code) {
        for (CouponUsedStatusEnum e : CouponUsedStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

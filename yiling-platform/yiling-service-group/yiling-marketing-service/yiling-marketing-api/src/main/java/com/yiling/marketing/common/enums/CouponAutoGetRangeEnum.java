package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 限制类型
 *
 * @author: houjie.sun
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum CouponAutoGetRangeEnum {

    /**
     * 1-全部
     */
    ALL(1, "全部"),
    /**
     * 2-部分
     */
    PART(2, "指定部分"),

    /**
     * 2-指定范围
     */
    range(3, "指定范围"),
    ;

    private Integer code;
    private String  name;

    public static CouponAutoGetRangeEnum getByCode(Integer code) {
        for (CouponAutoGetRangeEnum e : CouponAutoGetRangeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

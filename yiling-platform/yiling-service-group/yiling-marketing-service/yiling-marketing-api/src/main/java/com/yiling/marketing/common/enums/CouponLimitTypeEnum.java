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
public enum CouponLimitTypeEnum {

    /**
     * 1-全部
     */
    ALL(1, "全部"),
    /**
     * 2-部分
     */
    PART(2, "部分"),
    ;

    private Integer code;
    private String  name;

    public static CouponLimitTypeEnum getByCode(Integer code) {
        for (CouponLimitTypeEnum e : CouponLimitTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

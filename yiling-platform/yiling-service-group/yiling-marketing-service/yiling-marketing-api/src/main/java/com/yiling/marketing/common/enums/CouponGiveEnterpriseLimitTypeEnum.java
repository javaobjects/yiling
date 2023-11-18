package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2021/11/6
 */
@Getter
@AllArgsConstructor
public enum CouponGiveEnterpriseLimitTypeEnum {

    /**
     * 1-全部商家
     */
    ALL_ENTERPRISE(1, "全部商家"),
    /**
     * 2-部分商家
     */
    PART_ENTERPRISE(2, "部分商家"),
    ;

    private Integer code;
    private String  name;

    public static CouponGiveEnterpriseLimitTypeEnum getByCode(Integer code) {
        for (CouponGiveEnterpriseLimitTypeEnum e : CouponGiveEnterpriseLimitTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

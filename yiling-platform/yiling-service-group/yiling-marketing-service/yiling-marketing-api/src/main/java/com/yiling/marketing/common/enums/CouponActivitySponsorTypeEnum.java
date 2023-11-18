package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2021/11/8
 */
@Getter
@AllArgsConstructor
public enum CouponActivitySponsorTypeEnum {

    /**
     * 1-平台活动
     */
    PLATFORM(1, "平台活动"),
    /**
     * 2-商家活动
     */
    BUSINESS(2, "商家活动"),
    ;

    private Integer code;
    private String  name;

    public static CouponActivitySponsorTypeEnum getByCode(Integer code) {
        for (CouponActivitySponsorTypeEnum e : CouponActivitySponsorTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

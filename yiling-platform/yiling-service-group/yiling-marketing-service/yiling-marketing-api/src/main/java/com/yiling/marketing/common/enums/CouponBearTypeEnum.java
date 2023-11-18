package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 费用承担方类型
 *
 * @author: houjie.sun
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum CouponBearTypeEnum {

    /**
     * 1-平台
     */
    PLATFORM(1, "平台"),
    /**
     * 2-商家
     */
    BUSINESS(2, "商家"),
    /**
     * 3-共同承担
     */
    TOGETHER(3, "共同承担"),
    ;

    private Integer code;
    private String  name;

    public static CouponBearTypeEnum getByCode(Integer code) {
        for (CouponBearTypeEnum e : CouponBearTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

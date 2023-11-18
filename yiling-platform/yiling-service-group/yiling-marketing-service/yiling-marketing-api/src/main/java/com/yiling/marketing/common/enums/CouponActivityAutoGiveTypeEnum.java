package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2021/11/8
 */
@Getter
@AllArgsConstructor
public enum CouponActivityAutoGiveTypeEnum {

    // 1-订单累积金额
    ORDER_ACCUMULATE_AMOUNT (1, "订单累积金额"),
    // 2-会员自动发券
    MEMBER_AUTO (2, "会员自动发券"),
    // 3-推广企业自动发券
    ENTERPRISE_POPULARIZE (3, "推广企业自动发券"),
    ;

    private Integer code;
    private String name;

    public static CouponActivityAutoGiveTypeEnum getByCode(Integer code) {
        for (CouponActivityAutoGiveTypeEnum e : CouponActivityAutoGiveTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

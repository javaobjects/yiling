package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会员自动发券校验
 * @author: fan.shen
 * @date: 2022/1/7
 */
@Getter
@AllArgsConstructor
public enum CouponActivityAutoGiveCheckEnum {

    CHECK_COUPON_ACTIVITY_LIST (1,      "校验是否包含优惠券活动"),

    CHECK_ENTERPRISE_TYPE_LIMIT (2,     "校验是否包含企业类型"),

    CHECK_PROMOTION_CODE (3,            "校验推广码"),

    CHECK_ENTERPRISE_LIMIT (4,          "校验是否包含部分会员"),
    ;

    private Integer code;

    private String name;

    public static CouponActivityAutoGiveCheckEnum getByCode(Integer code) {
        for (CouponActivityAutoGiveCheckEnum e : CouponActivityAutoGiveCheckEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2021/10/26
 */
@Getter
@AllArgsConstructor
public enum MemberCouponActivityStatusEnum {

    // 1-商品券
    GOODS_ACTIVITY(1, "商品券"),
    // 2-商家券
    MEMBER_ACTIVITY(2, "商家券"),
    ;

    private Integer code;
    private String name;

    public static MemberCouponActivityStatusEnum getByCode(Integer code) {
        for (MemberCouponActivityStatusEnum e : MemberCouponActivityStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

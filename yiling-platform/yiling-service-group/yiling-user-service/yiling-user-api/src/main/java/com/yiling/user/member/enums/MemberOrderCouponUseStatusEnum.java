package com.yiling.user.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会员订单优惠券使用状态枚举
 *
 * @author: lun.yu
 * @date: 2022-08-19
 */
@Getter
@AllArgsConstructor
public enum MemberOrderCouponUseStatusEnum {

    // 使用状态：1-已使用 2-已退回
    USED(1, "已使用"),
    RETURN(2, "已退回"),
    ;

    private final Integer code;
    private final String name;

    public static MemberOrderCouponUseStatusEnum getByCode(Integer code) {
        for (MemberOrderCouponUseStatusEnum e : MemberOrderCouponUseStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

package com.yiling.user.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会员订单状态枚举
 *
 * @author: lun.yu
 * @date: 2021/10/27
 */
@Getter
@AllArgsConstructor
public enum MemberOrderStatusEnum {

    /**
     * 支付状态
     */
    WAITING_PAY(10, "待支付"),
    PAY_SUCCESS(20, "支付成功"),
    PAY_FAIL(30, "支付失败"),
    ;

    private final Integer code;
    private final String name;

    public static MemberOrderStatusEnum getByCode(Integer code) {
        for (MemberOrderStatusEnum e : MemberOrderStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

package com.yiling.user.integral.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单送积分-是否区分支付方式枚举
 *
 * @author: lun.yu
 * @date: 2022-12-30
 */
@Getter
@AllArgsConstructor
public enum IntegralPaymentMethodFlagEnum {

    // 是否区分支付方式：1-全部支付方式 2-指定支付方式
    ALL(1, "全部支付方式"),
    ASSIGN(2, "指定支付方式"),
    ;

    private final Integer code;
    private final String name;

    public static IntegralPaymentMethodFlagEnum getByCode(Integer code) {
        for (IntegralPaymentMethodFlagEnum e : IntegralPaymentMethodFlagEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

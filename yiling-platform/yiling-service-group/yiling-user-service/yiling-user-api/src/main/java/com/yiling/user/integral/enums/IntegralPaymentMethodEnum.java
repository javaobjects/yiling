package com.yiling.user.integral.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单送积分-支付方式枚举
 *
 * @author: lun.yu
 * @date: 2022-12-30
 */
@Getter
@AllArgsConstructor
public enum IntegralPaymentMethodEnum {

    // 支付方式：1-线上支付 2-线下支付 3-账期支付
    ONLINE(1, "线上支付"),
    OFFLINE(2, "线下支付"),
    PAYMENT_DAYS(3, "账期支付"),
    ;

    private final Integer code;
    private final String name;

    public static IntegralPaymentMethodEnum getByCode(Integer code) {
        for (IntegralPaymentMethodEnum e : IntegralPaymentMethodEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

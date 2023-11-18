package com.yiling.hmc.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付方式:1-保险理赔结算
 *
 * @author: fan.shen
 * @date: 2022/4/7
 */
@Getter
@AllArgsConstructor
public enum HmcPaymentMethodEnum {

    /**
     * 1-保险理赔结算
     */
    INSURANCE_PAY(1, "保险理赔结算"),

    /**
     * 2-微信支付
     */
    WECHAT_PAY(2, "微信支付"),
    ;

    private final Integer code;
    private final String name;

    public static HmcPaymentMethodEnum getByCode(Integer code) {
        for (HmcPaymentMethodEnum e : HmcPaymentMethodEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付方式枚举
 *
 * @author: xuan.zhou
 * @date: 2021/6/28
 */
@Getter
@AllArgsConstructor
public enum PaymentMethodEnum {

    OFFLINE(1L, "线下支付"),
    PAYMENT_DAYS(2L, "账期"),
    PREPAYMENT(3L, "预付款"),
    ONLINE(4L, "在线支付"),
    ;

    private Long code;
    private String name;

    public static PaymentMethodEnum getByCode(Long code) {
        for (PaymentMethodEnum e : PaymentMethodEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

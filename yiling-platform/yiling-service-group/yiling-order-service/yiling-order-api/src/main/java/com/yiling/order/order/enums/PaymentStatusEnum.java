package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付状态枚举
 *
 * @author: xuan.zhou
 * @date: 2021/6/25
 */
@Getter
@AllArgsConstructor
public enum PaymentStatusEnum {

    UNPAID(1, "待支付"),
    PARTPAID(2, "部分支付"),
    PAID(3, "已支付"),
    PAYMENT_DAY_RETURN(4, "商家确认回款"),
    ;

    private Integer code;
    private String name;

    public static PaymentStatusEnum getByCode(Integer code) {
        for (PaymentStatusEnum e : PaymentStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

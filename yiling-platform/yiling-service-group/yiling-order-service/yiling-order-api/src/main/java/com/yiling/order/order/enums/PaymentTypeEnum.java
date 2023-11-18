package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付类型枚举
 *
 * @author: xuan.zhou
 * @date: 2021/6/25
 */
@Getter
@AllArgsConstructor
public enum PaymentTypeEnum {

    OFFLINE(1, "线下支付"),
    ONLINE(2, "在线支付"),
    ;

    private Integer code;
    private String name;

    public static PaymentTypeEnum getByCode(Integer code) {
        for (PaymentTypeEnum e : PaymentTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

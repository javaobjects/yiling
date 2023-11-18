package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单类型枚举
 *
 * @author: xuan.zhou
 * @date: 2021/6/25
 */
@Getter
@AllArgsConstructor
public enum OrderTypeEnum {

    POP(1, "POP订单"),
    B2B(2, "B2B订单"),
    ;

    private Integer code;
    private String name;

    public static OrderTypeEnum getByCode(Integer code) {
        for (OrderTypeEnum e : OrderTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

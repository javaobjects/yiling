package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单来源枚举
 *
 * @author: xuan.zhou
 * @date: 2021/6/25
 */
@Getter
@AllArgsConstructor
public enum OrderSourceEnum {

    POP_PC(1, "POP-PC平台"),
    POP_APP(2, "POP-APP平台"),
    B2B_APP(3, "B2B-APP平台"),
    SA(4, "销售助手-APP平台")
    ;

    private Integer code;
    private String name;

    public static OrderSourceEnum getByCode(Integer code) {
        for (OrderSourceEnum e : OrderSourceEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

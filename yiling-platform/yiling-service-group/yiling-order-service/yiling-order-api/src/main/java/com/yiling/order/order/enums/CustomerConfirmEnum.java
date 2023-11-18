package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单客户确认状态
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.enums
 * @date: 2021/9/18
 */
@Getter
@AllArgsConstructor
public enum  CustomerConfirmEnum {

    TRANSFER(-20,"待转发"),
    NOTCONFIRM(-30,"待确认"),
    CONFIRMED(-40,"已确认"),
    ;

    private Integer code;
    private String name;

    public static CustomerConfirmEnum getByCode(Integer code) {
        for (CustomerConfirmEnum e : CustomerConfirmEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

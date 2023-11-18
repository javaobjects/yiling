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
public enum OrderLogTypeEnum {

    ORDER_ORDINARY_LOG(1, "普通日志"),
    ORDER_RETREAT_LOG(2, "退货日志"),
    ;

    private Integer code;
    private String name;

    public static OrderLogTypeEnum getByCode(Integer code) {
        for (OrderLogTypeEnum e : OrderLogTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

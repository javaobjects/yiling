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
public enum OrderTicketDiscountFlagEnum {

    YES(1, "是"),
    FALSE(0, "否"),
    ;

    private Integer code;
    private String name;

    public static OrderTicketDiscountFlagEnum getByCode(Integer code) {
        for (OrderTicketDiscountFlagEnum e : OrderTicketDiscountFlagEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

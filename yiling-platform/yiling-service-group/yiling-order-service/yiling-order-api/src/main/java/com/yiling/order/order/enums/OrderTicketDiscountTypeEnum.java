package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否使用票折
 *
 * @author:wei.wang
 * @date:2021/7/27
 */
@Getter
@AllArgsConstructor
public enum OrderTicketDiscountTypeEnum {

    NOT_USE_TICKET_DISCOUNT(0, "否"),
    USE_TICKET_DISCOUNT(1, "是"),
    ;

    private Integer code;
    private String  name;

    public static OrderTicketDiscountTypeEnum getByCode(Integer code) {
        for (OrderTicketDiscountTypeEnum e : OrderTicketDiscountTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

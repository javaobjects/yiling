package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhigang.guo
 * @date: 2022/10/10
 */
@Getter
@AllArgsConstructor
public enum OrderCategoryEnum {

    NORMAL(1, "正常订单"), PRESALE(2, "预售订单"),
    ;

    private Integer code;
    private String name;

    public static OrderCategoryEnum getByCode(Integer code) {
        for (OrderCategoryEnum e : OrderCategoryEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

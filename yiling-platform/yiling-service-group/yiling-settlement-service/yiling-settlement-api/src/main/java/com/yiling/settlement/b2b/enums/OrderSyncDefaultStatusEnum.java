package com.yiling.settlement.b2b.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 预售违约状态 1-履约 2-违约
 *
 * @author: dexi.yao
 * @date: 2022-04-08
 */
@Getter
@AllArgsConstructor
public enum OrderSyncDefaultStatusEnum {

    //履约
    ORDER_PERFORMANCE(1, "履约"),
    //违约
    ORDER_DEFAULT(2, "违约");

    private Integer code;
    private String  name;

    public static OrderSyncDefaultStatusEnum getByCode(Integer code) {
        for (OrderSyncDefaultStatusEnum e : OrderSyncDefaultStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

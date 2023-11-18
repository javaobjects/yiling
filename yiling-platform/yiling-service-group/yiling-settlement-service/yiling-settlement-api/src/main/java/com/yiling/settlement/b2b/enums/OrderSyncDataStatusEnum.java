package com.yiling.settlement.b2b.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否可生成结算单：1-不可生成 2-可生成
 * @author: dexi.yao
 * @date: 2022-04-08
 */
@Getter
@AllArgsConstructor
public enum OrderSyncDataStatusEnum {
    //不可生成
    UNAUTHORIZED(1, "不可生成"),
    //可生成
    AUTHORIZED(2, "可生成");

    private Integer code;
    private String  name;

    public static OrderSyncDataStatusEnum getByCode(Integer code) {
        for (OrderSyncDataStatusEnum e : OrderSyncDataStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

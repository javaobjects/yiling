package com.yiling.settlement.b2b.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: dexi.yao
 * @date: 2022-04-08
 */
@Getter
@AllArgsConstructor
public enum OrderSyncStatusEnum {
    //失败
    FAIL(1, "失败"),
    //成功
    SUCCESS(2, "成功");

    private Integer code;
    private String  name;

    public static OrderSyncStatusEnum getByCode(Integer code) {
        for (OrderSyncStatusEnum e : OrderSyncStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

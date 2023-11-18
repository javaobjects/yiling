package com.yiling.open.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GoodsStatusRule {

    DEFAULT_IN(0,"默认上架"),
    INVENTORY_IN(1,"库存上下架"),
            ;

    private Integer code;
    private String message;

    public static GoodsStatusRule getFromCode(Integer code) {
        for(GoodsStatusRule e: GoodsStatusRule.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

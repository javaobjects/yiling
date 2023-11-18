package com.yiling.hmc.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单类型:1-八子，2-处方订单
 *
 * @author: fan.shen
 * @date: 2022/4/7
 */
@Getter
@AllArgsConstructor
public enum HmcMarketOrderTypeEnum {

    BA_ZI_ORDER(1, "八子订单"),

    PRESCRIPTION_ORDER(2, "处方订单"),

    ;

    private final Integer code;
    private final String name;

    public static HmcMarketOrderTypeEnum getByCode(Integer code) {
        for (HmcMarketOrderTypeEnum e : HmcMarketOrderTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

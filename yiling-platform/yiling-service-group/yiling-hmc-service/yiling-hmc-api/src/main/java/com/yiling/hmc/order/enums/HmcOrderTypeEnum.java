package com.yiling.hmc.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单类型:1-其他/2-虚拟商品订单/3-普通商品/4-药品订单
 *
 * @author: fan.shen
 * @date: 2022/4/7
 */
@Getter
@AllArgsConstructor
public enum HmcOrderTypeEnum {

    OTHER(1, "其他"),

    VIRTUAL(2, "虚拟商品订单"),

    NORMAL(3, "普通商品"),

    MEDICINE(4, "药品订单"),
    ;

    private final Integer code;
    private final String name;

    public static HmcOrderTypeEnum getByCode(Integer code) {
        for (HmcOrderTypeEnum e : HmcOrderTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

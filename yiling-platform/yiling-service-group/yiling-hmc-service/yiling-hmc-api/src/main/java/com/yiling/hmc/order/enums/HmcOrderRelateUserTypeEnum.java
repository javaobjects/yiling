package com.yiling.hmc.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单相关人
 *
 * @author: fan.shen
 * @date: 2022/4/27
 */
@Getter
@AllArgsConstructor
public enum HmcOrderRelateUserTypeEnum {

    /**
     * 1-收货人
     */
    RECEIVER(1, "收货人"),

    /**
     * 2-发货人
     */
    DELIVER(2, "发货人"),
    ;

    private final Integer code;

    private final String name;

    public static HmcOrderRelateUserTypeEnum getByCode(Integer code) {
        for (HmcOrderRelateUserTypeEnum e : HmcOrderRelateUserTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

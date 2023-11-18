package com.yiling.hmc.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 配送方式 1-自提 2-物流
 *
 * @author: fan.shen
 * @date: 2022/4/7
 */
@Getter
@AllArgsConstructor
public enum HmcDeliveryTypeEnum {

    /**
     * 1-自提
     */
    SELF_PICKUP(1, "自提"),

    /**
     * 2-快递
     */
    FREIGHT(2, "快递"),
    ;

    private final Integer code;
    private final String name;

    public static HmcDeliveryTypeEnum getByCode(Integer code) {
        for (HmcDeliveryTypeEnum e : HmcDeliveryTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

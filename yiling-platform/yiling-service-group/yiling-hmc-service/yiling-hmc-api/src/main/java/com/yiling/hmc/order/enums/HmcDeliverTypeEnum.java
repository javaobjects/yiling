package com.yiling.hmc.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: yong.zhang
 * @date: 2022/3/30
 */
@Getter
@AllArgsConstructor
public enum HmcDeliverTypeEnum {

    /**
     * 自提
     */
    SELF(1, "自提"),
    /**
     * 物流
     */
    DELIVER(2, "物流"),
    ;

    private final Integer code;
    private final String name;

    public static HmcDeliverTypeEnum getByCode(Integer code) {
        for (HmcDeliverTypeEnum e : HmcDeliverTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

package com.yiling.hmc.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 投保人、被保人
 *
 * @author: fan.shen
 * @date: 2022/4/20
 */
@Getter
@AllArgsConstructor
public enum HmcHolderTypeEnum {

    /**
     * 1-被保人
     */
    ISSUE(1, "被保人"),

    /**
     * 2-投保人
     */
    HOLDER(2, "投保人"),
    ;

    private final Integer code;

    private final String name;

    public static HmcHolderTypeEnum getByCode(Integer code) {
        for (HmcHolderTypeEnum e : HmcHolderTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

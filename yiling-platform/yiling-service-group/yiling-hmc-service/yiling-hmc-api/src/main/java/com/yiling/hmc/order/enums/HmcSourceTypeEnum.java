package com.yiling.hmc.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 来源类型 0-线上渠道；1-线下渠道
 *
 * @author: fan.shen
 * @date: 2022/4/21
 */
@Getter
@AllArgsConstructor
public enum HmcSourceTypeEnum {

    /**
     * 0-线上来源
     */
    ONLINE(0, "线上来源"),

    /**
     * 1-线下来源
     */
    OFFLINE(1, "线下来源"),
    ;

    private final Integer code;

    private final String name;

    public static HmcSourceTypeEnum getByCode(Integer code) {
        for (HmcSourceTypeEnum e : HmcSourceTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

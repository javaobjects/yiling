package com.yiling.hmc.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 创建来源 1-商家后台，2-C端小程序
 *
 * @author: fan.shen
 * @date: 2022/4/22
 */
@Getter
@AllArgsConstructor
public enum HmcCreateSourceEnum {

    /**
     * 1-商家后台
     */
    ADMIN_HMC(1, "商家后台"),

    /**
     * 2-C端小程序
     */
    HMC_MA(2, "C端小程序"),

    /**
     * 3-C端公众号
     */
    HMC_MP(3, "C端公众号"),
    ;

    private final Integer code;
    private final String name;

    public static HmcCreateSourceEnum getByCode(Integer code) {
        for (HmcCreateSourceEnum e : HmcCreateSourceEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

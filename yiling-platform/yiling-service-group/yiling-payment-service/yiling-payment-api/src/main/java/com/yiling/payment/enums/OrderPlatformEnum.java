package com.yiling.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单平台来源
 *
 * @author zhigang.guo
 * @date: 2023/2/1
 */
@Getter
@AllArgsConstructor
public enum OrderPlatformEnum {

    B2B("b2b", "大运河"), HMC("hmc", "健康管理中心"),
    ;

    private String code;

    private String name;

    public static OrderPlatformEnum getByCode(String code) {

        for (OrderPlatformEnum e : OrderPlatformEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

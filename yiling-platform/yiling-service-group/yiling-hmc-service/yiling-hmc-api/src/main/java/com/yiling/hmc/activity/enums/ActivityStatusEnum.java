package com.yiling.hmc.activity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 活动状态
 *
 * @author: fan.shen
 * @date: 2023-01-13
 */
@Getter
@AllArgsConstructor
public enum ActivityStatusEnum {

    /**
     * 启用
     */
    ENABLE(1, "启用"),

    /**
     * 停用
     */
    UNABLE(2, "停用"),

    ;

    private final Integer code;
    private final String name;

    public static ActivityStatusEnum getByCode(Integer code) {
        for (ActivityStatusEnum e : ActivityStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

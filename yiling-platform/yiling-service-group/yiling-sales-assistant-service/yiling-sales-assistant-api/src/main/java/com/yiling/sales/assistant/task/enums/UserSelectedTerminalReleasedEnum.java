package com.yiling.sales.assistant.task.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: hongyang.zhang
 * @data: 2021/09/15
 */
@Getter
@AllArgsConstructor
public enum UserSelectedTerminalReleasedEnum {
    /**
     * 占用
     */
    LOCK(0,"占用"),

    /**
     * 释放
     */
    UNLOCK(1,"释放");

    private Integer code;
    private String  name;

    public static UserSelectedTerminalReleasedEnum getByCode(Integer code) {
        for (UserSelectedTerminalReleasedEnum e : UserSelectedTerminalReleasedEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

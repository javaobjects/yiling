package com.yiling.dataflow.gb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 团购锁定类型枚举类
 *
 * @author: houjie.sun
 * @date: 2023/6/2
 */
@Getter
@AllArgsConstructor
public enum GbLockTypeEnum {

    LOCK(1, "锁定"),
    UN_LOCK(2, "非锁定"),
    ;

    private final Integer code;
    private final String message;

    public static GbLockTypeEnum getFromCode(Integer code) {
        for (GbLockTypeEnum e : GbLockTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}

package com.yiling.dataflow.gb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 团购处理类型枚举类
 *
 * @author: houjie.sun
 * @date: 2023/5/18
 */
@Getter
@AllArgsConstructor
public enum GbExecTypeEnum {

    AUTO(1, "自动"),
    ARTIFICIAL(2, "人工"),
    ;

    private final Integer code;
    private final String message;

    public static GbExecTypeEnum getFromCode(Integer code) {
        for (GbExecTypeEnum e : GbExecTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

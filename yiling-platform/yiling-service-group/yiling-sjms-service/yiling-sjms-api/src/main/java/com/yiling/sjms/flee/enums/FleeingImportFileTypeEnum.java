package com.yiling.sjms.flee.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件类型 1-申报 2-申报确认
 *
 * @author: yong.zhang
 * @date: 2023/3/20 0020
 */
@Getter
@AllArgsConstructor
public enum FleeingImportFileTypeEnum {

    /**
     * 申报
     */
    FLEEING(1, "申报"),
    /**
     * 申报确认
     */
    FLEEING_CONFIRM(2, "申报确认"),
    ;
    private final Integer code;
    private final String name;

    public static FleeingImportFileTypeEnum getByCode(Integer code) {
        for (FleeingImportFileTypeEnum e : FleeingImportFileTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

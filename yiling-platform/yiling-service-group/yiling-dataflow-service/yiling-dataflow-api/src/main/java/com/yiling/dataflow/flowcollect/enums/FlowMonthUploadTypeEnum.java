package com.yiling.dataflow.flowcollect.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: gxl
 * @date: 2023/6/28
 */
@Getter
@AllArgsConstructor
public enum FlowMonthUploadTypeEnum {
    NORMAL(1,"正常"),
    FIX(2,"补传"),;

    private final Integer code;
    private final String name;

    public static FlowMonthUploadTypeEnum getByCode(Integer code) {
        for(FlowMonthUploadTypeEnum e: FlowMonthUploadTypeEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

package com.yiling.sales.assistant.task.enums;

import lombok.Getter;

/**
 * @author gxl
 * 计算方式
 */
@Getter
public enum ComputeTypeEnum {
    MORE(1,"单品计算"),
    SINGLE(2,"多品计算"),
    ;
    private Integer code;
    private String desc;

    ComputeTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

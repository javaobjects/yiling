package com.yiling.sales.assistant.task.enums;

import lombok.Getter;

/**
 * 销售类型
 * @author gxl
 */
@Getter
public enum SaleTypeEnum {
    SINGLE(0,"单品销售"),
    MORE(1,"多品销售"),
    ;
    private Integer code;
    private String desc;

    SaleTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

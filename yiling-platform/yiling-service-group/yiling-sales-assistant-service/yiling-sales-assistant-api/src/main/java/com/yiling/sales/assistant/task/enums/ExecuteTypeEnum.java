package com.yiling.sales.assistant.task.enums;

import lombok.Getter;

/**
 * @author gxl
 * 佣金执行政策（只针对 交易额和交易量条件下的）
 */
@Getter
public enum ExecuteTypeEnum {
    MORE(0,"统一执行"),
    SINGLE(1,"单独执行"),
    ;
    private Integer code;
    private String desc;

    ExecuteTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

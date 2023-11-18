package com.yiling.sales.assistant.task.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务类型
 * @author: hongyang.zhang
 * @data: 2021/09/13
 */
@Getter
@AllArgsConstructor
public enum TaskTypeEnum {
    /**
     * 平台任务
     */
    PLATFORM(0,"平台任务"),

    /**
     * 企业任务
     */
    ENTERPRISE(1,"企业任务");

    private Integer code;
    private String  name;

    public static TaskTypeEnum getByCode(Integer code) {
        for (TaskTypeEnum e : TaskTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

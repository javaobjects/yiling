package com.yiling.dataflow.wash.enums;

import lombok.Getter;

/**
 * @author fucheng.bai
 * @date 2023/3/9
 */
@Getter
public enum FlowWashTaskStatusEnum {

    NOT_WASH(1, "未清洗"),

    WASHING(2, "清洗中"),

    FINISH_WASH(3, "已完成"),

    FAIL(4, "清洗失败"),

    DEPRECATED(5, "已弃用"),
    ;
    private Integer code;
    private String desc;

    FlowWashTaskStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

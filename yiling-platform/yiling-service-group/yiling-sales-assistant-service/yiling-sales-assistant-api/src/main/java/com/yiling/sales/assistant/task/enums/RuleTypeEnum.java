package com.yiling.sales.assistant.task.enums;

import lombok.Getter;

@Getter
public enum RuleTypeEnum {
    TAKE(0,"参与条件"),
    FINISH_TYPE(1,"完成条件"),
    COMMISSION(2,"佣金设置"),
    ;
    private Integer code;
    private String desc;

    RuleTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

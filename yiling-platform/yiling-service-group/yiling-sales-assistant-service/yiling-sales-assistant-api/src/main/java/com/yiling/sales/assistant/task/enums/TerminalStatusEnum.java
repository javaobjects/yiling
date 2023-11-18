package com.yiling.sales.assistant.task.enums;

import lombok.Getter;

/**
 * @author gxl
 * 终端状态
 */
@Getter
public enum TerminalStatusEnum {
    SELECT(0,"可选"),
    SELECED(1,"自己已选"),
    OTHER_SELECTED(2,"被别人选中"),
    ;

    private Integer status;

    private String desc;

    TerminalStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}

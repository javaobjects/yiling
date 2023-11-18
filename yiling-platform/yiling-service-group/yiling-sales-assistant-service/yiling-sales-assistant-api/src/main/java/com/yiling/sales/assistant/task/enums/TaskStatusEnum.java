package com.yiling.sales.assistant.task.enums;

import lombok.Getter;

/**
 * @author gxl
 * @date 2021-9-13
 * 任务状态
 */
@Getter
public enum TaskStatusEnum {
    UNSTART(0,"未开始"),
    IN_PROGRESS(1,"进行中"),
    END(2,"已结束"),
    STOP(3,"停止"),
    ;

    private Integer status;

    private String desc;

    TaskStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}

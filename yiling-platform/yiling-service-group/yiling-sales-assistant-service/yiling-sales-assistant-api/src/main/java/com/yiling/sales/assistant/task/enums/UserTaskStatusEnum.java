package com.yiling.sales.assistant.task.enums;

import lombok.Getter;

/**
 * @author gxl
 * 用户任务状态
 */
@Getter
public enum UserTaskStatusEnum {
    IN_PROGRESS(0,"进行中"),
    FINISHED(1,"已完成"),
    UN_FINISH(2,"未完成"),
    STOP(3,"停止"),
    ;

    private Integer status;

    private String desc;

    UserTaskStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}

package com.yiling.dataflow.wash.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2022/12/12
 */
@Getter
@AllArgsConstructor
public enum TaskStatusEnum {
    //日程状态：1未开始2已备份3已生成任务4结束
    NOT_HANDLE(0,"未处理"),
    HANDLE(1,"已处理"),
    ;

    private Integer code;
    private String desc;

    public static TaskStatusEnum getByCode(Integer code) {
        for(TaskStatusEnum e: TaskStatusEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}

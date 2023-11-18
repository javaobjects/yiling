package com.yiling.dataflow.wash.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2022/12/12
 */
@Getter
@AllArgsConstructor
public enum FlowMonthWashControlStatusEnum {
    //日程状态：1未开始2已备份3已生成任务4结束
    NOT_OPEN(1,"未开启"),
    FINISH (4,"结束"),
    ;

    private Integer code;
    private String desc;

    public static FlowMonthWashControlStatusEnum getByCode(Integer code) {
        for(FlowMonthWashControlStatusEnum e: FlowMonthWashControlStatusEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}

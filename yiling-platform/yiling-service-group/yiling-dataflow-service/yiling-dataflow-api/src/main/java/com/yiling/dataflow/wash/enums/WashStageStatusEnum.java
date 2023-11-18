package com.yiling.dataflow.wash.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 清洗日程阶段状态枚举类
 *
 * @author: houjie.sun
 * @date: 2023/6/8
 */
@Getter
@AllArgsConstructor
public enum WashStageStatusEnum {

    // 日程任务状态：1-未开始 2-进行中 3-已完成
    UN_START(1,"未开始"),
    IN_PROGRESS(2,"进行中"),
    FINISH(3,"已完成"),
    ;

    private Integer code;
    private String desc;

    public static WashStageStatusEnum getByCode(Integer code) {
        for(WashStageStatusEnum e: WashStageStatusEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

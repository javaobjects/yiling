package com.yiling.dataflow.wash.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2022/12/12
 */
@Getter
@AllArgsConstructor
public enum FlowMonthWashControlMappingStatusEnum {
    NOT_OPEN(1,"未开启"),
    IN_PROGRESS(2,"进行中"),
    CLOSE(3,"过期关闭"),
    MANUAL_OPEN (4,"手动开启"),
    MANUAL_CLOSE(5,"手动关闭"),
    ;

    private Integer code;
    private String desc;

    public static FlowMonthWashControlMappingStatusEnum getByCode(Integer code) {
        for(FlowMonthWashControlMappingStatusEnum e: FlowMonthWashControlMappingStatusEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}

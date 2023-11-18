package com.yiling.open.monitor.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2022/10/18
 */
@Getter
@AllArgsConstructor
public enum MonitorAbnormalDataTypeEnum {

    EXCEPTION(1,"超过3天以后上传的数据"),
    NORMAL(2,"原始数据"),
    ;

    private Integer code;
    private String desc;

    public static MonitorAbnormalDataTypeEnum getFromCode(Integer code) {
        for(MonitorAbnormalDataTypeEnum e: MonitorAbnormalDataTypeEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

package com.yiling.dataflow.wash.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2022/12/12
 */
@Getter
@AllArgsConstructor
public enum BackupStatusEnum {
    //日程状态：1未开始2已备份3已生成任务4结束
    NOT_BACK_UP(0,"未备份"),
    BACK_UP(1,"已备份"),
    ;

    private Integer code;
    private String desc;

    public static BackupStatusEnum getByCode(Integer code) {
        for(BackupStatusEnum e: BackupStatusEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}

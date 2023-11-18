package com.yiling.dataflow.flowcollect.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 月流向上传检查状态枚举
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-04
 */
@Getter
@AllArgsConstructor
public enum FlowMonthUploadCheckStatusEnum {

    PASS(1,"通过"),
    NOT_PASS(2,"未通过"),
    WARN(3,"警告"),
    ;

    private final Integer code;
    private final String name;

    public static FlowMonthUploadCheckStatusEnum getByCode(Integer code) {
        for(FlowMonthUploadCheckStatusEnum e: FlowMonthUploadCheckStatusEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

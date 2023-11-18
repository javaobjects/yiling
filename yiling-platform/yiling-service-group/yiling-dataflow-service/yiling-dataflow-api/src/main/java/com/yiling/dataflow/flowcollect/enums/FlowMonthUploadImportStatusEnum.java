package com.yiling.dataflow.flowcollect.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 月流向上传导入状态枚举
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-04
 */
@Getter
@AllArgsConstructor
public enum FlowMonthUploadImportStatusEnum {

    SUCCESS(1,"导入成功"),
    FAIL(2,"导入失败"),
    IN_PROGRESS(3,"导入中"),
    ;

    private final Integer code;
    private final String name;

    public static FlowMonthUploadImportStatusEnum getByCode(Integer code) {
        for(FlowMonthUploadImportStatusEnum e: FlowMonthUploadImportStatusEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

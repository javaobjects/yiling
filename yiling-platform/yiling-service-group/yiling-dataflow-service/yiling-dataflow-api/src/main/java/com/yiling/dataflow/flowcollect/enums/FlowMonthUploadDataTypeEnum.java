package com.yiling.dataflow.flowcollect.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 月流向上传数据类型枚举
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-06
 */
@Getter
@AllArgsConstructor
public enum FlowMonthUploadDataTypeEnum {

    // 流向数据类型：1-销售 2-库存 3-采购
    SALES(1,"销售"),
    INVENTORY(2,"库存"),
    PURCHASE(3,"采购"),
    ;

    private final Integer code;
    private final String name;

    public static FlowMonthUploadDataTypeEnum getByCode(Integer code) {
        for(FlowMonthUploadDataTypeEnum e: FlowMonthUploadDataTypeEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

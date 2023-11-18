package com.yiling.sales.assistant.task.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 随货同行单流向匹配结果
 * @author: gxl
 * @date: 2023/1/31
 */
@Getter
@AllArgsConstructor
public enum FlowMatchResultEnum {
    ERP_CRM_EQ(1,"ERP与CRM流向一致"),

    ERP_CRM_NQ(2,"ERP与CRM流向不一致"),


    NONE(3,"无流向"),

    TO_MATCH(4,"待匹配流向"),
    ;
    private final Integer code;
    private final String name;
}

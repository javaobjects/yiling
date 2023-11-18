package com.yiling.dataflow.sale.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: gxl
 * @date: 2023/4/21
 */
@Getter
@AllArgsConstructor
public enum ResolveToastEnum {
    SAME(1,"分解完成"),

    NOT_SAME(2,"分解明细合计与部门总目标不一致"),

    UN_RESOLVE(3,"分解明细中有未分解数据"),
    ;

    private Integer code;
    private String desc;
}

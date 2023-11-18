package com.yiling.dataflow.sale.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: gxl
 * @date: 2023/4/20
 */
@Getter
@AllArgsConstructor
public enum ResolveStatusEnum {
    TO_RESOVLE(1,"未分解"),
    RESOVLED(2,"已分解"),;


    private Integer code;
    private String desc;

    public static ResolveStatusEnum getByCode(Integer code) {
        for (ResolveStatusEnum e : ResolveStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
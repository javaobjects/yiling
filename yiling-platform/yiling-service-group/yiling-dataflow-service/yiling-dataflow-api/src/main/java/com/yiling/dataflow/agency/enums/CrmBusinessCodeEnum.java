package com.yiling.dataflow.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务状态 1有效 2失效
 *
 * @author: yong.zhang
 * @date: 2023/2/17 0017
 */
@Getter
@AllArgsConstructor
public enum CrmBusinessCodeEnum {
    /**
     * 1有效
     */
    CHAIN(1, "有效"),
    /**
     * 2失效
     */
    SIMPLE(2, "失效"),
    ;

    private final Integer code;
    private final String name;

    public static CrmBusinessCodeEnum getByCode(Integer code) {
        for (CrmBusinessCodeEnum e : CrmBusinessCodeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

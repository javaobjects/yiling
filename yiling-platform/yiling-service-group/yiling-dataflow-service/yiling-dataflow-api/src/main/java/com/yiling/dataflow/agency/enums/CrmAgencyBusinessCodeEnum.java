package com.yiling.dataflow.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 基础数据业务状态：1有效 2失效
 *
 * @author: yong.zhang
 * @date: 2023/2/16 0016
 */
@Getter
@AllArgsConstructor
public enum CrmAgencyBusinessCodeEnum {

    VALID (1, "有效"),

    INVALID(2, "无效"),
    ;

    private final Integer code;
    private final String name;

    public static CrmAgencyBusinessCodeEnum getByCode(Integer code) {
        for (CrmAgencyBusinessCodeEnum e : CrmAgencyBusinessCodeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

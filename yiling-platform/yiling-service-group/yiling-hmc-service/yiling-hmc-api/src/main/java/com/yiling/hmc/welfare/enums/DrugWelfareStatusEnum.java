package com.yiling.hmc.welfare.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 药品福利计划状态 1-启用，2-停用
 *
 * @author: fan.shen
 * @date: 2022-09-27
 */
@Getter
@AllArgsConstructor
public enum DrugWelfareStatusEnum {

    /**
     * 启用
     */
    VALID(1, "启用"),

    /**
     * 停用
     */
    INVALID(2, "停用"),


    ;

    private final Integer code;

    private final String name;

    public static DrugWelfareStatusEnum getByCode(Integer code) {
        for (DrugWelfareStatusEnum e : DrugWelfareStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

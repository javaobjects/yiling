package com.yiling.hmc.welfare.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 药品福利计划类型 1-通心络
 *
 * @author: fan.shen
 * @date: 2022-09-27
 */
@Getter
@AllArgsConstructor
public enum DrugWelfareTypeEnum {

    /**
     * 通心络
     */
    TONG_XIN_LUO(1, "通心络"),



    ;

    private final Integer code;

    private final String name;

    public static DrugWelfareTypeEnum getByCode(Integer code) {
        for (DrugWelfareTypeEnum e : DrugWelfareTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

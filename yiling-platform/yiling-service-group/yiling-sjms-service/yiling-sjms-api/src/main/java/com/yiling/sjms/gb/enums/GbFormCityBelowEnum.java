package com.yiling.sjms.gb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 团购性质枚举
 *
 * @author: wei.wang
 * @date: 2022/12/06
 */
@Getter
@AllArgsConstructor
public enum GbFormCityBelowEnum {
    DEFAULT(0,""),
    YES(1, "是"),
    NO(2, "否"),
    ;

    private Integer code;
    private String name;

    public static GbFormCityBelowEnum getByCode(Integer code) {
        for (GbFormCityBelowEnum e : GbFormCityBelowEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

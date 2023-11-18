package com.yiling.sjms.gb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 团购区域枚举
 *
 * @author: wei.wang
 * @date: 2022/12/06
 */
@Getter
@AllArgsConstructor
public enum GbFormRegionTypeEnum {

    INTERNAL(1, "国内"),
    ABROAD(2, "国外"),
    ;

    private Integer code;
    private String name;

    public static GbFormRegionTypeEnum getByCode(Integer code) {
        for (GbFormRegionTypeEnum e : GbFormRegionTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

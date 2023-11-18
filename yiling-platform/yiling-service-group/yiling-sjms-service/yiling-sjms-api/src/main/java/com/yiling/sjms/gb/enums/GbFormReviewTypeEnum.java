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
public enum GbFormReviewTypeEnum {
    DEFAULT(0,""),
    ORDINARY(1, "普通团购"),
    COUNTRY(2, "政府团购"),
    ;

    private Integer code;
    private String name;

    public static GbFormReviewTypeEnum getByCode(Integer code) {
        for (GbFormReviewTypeEnum e : GbFormReviewTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

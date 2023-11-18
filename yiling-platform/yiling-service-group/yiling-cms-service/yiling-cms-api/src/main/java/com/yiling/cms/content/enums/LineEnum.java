package com.yiling.cms.content.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务线枚举
 *
 * @author: fan.shen
 * @date: 2022-11-10
 */
@Getter
@AllArgsConstructor
public enum LineEnum {

    HMC(1, "健康管理中心"),

    IH_DOC(2, "以岭互联网医院医生端"),

    IH_PATIENT(3, "以岭互联网医院患者端"),

    SA(6, "销售助手"),

    B2B(7, "大运河"),
    ;

    private final Integer code;
    private final String name;

    public static LineEnum getByCode(Integer code) {
        for (LineEnum e : LineEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

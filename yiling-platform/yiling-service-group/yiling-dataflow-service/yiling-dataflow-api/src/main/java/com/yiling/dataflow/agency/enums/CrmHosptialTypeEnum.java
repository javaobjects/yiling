package com.yiling.dataflow.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 医院性质 1公立 2私立 3厂办
 *
 * @author: shixing.sun
 * @date: 2023/2/17
 */
@Getter
@AllArgsConstructor
public enum CrmHosptialTypeEnum {
    /**
     * 1-直营
     */
    PUBLIC(1, "公立"),
    /**
     * 2-加盟
     */
    PRIVAGE(2, "私立"),

    FACTORY(3, "厂办"),

    ;

    private final Integer code;
    private final String name;

    public static CrmHosptialTypeEnum getByCode(Integer code) {
        for (CrmHosptialTypeEnum e : CrmHosptialTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

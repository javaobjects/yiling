package com.yiling.dataflow.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 药店级别 1-A级；2-B级；3-C级
 *
 * @author: yong.zhang
 * @date: 2023/2/16 0016
 */
@Getter
@AllArgsConstructor
public enum CrmPharmacyLevelEnum {

    /**
     * 1-A级
     */
    A(1, "A级"),
    /**
     * 2-B级
     */
    B(2, "B级"),
    /**
     * 3-C级
     */
    C(3, "C级"),
    ;

    private final Integer code;
    private final String name;

    public static CrmPharmacyLevelEnum getByCode(Integer code) {
        for (CrmPharmacyLevelEnum e : CrmPharmacyLevelEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

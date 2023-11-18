package com.yiling.dataflow.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 药店类型 1-直营；2-加盟
 *
 * @author: yong.zhang
 * @date: 2023/2/16 0016
 */
@Getter
@AllArgsConstructor
public enum CrmPharmacyTypeEnum {
    /**
     * 1-直营
     */
    DIRECT(1, "直营"),
    /**
     * 2-加盟
     */
    JOIN(2, "加盟"),
    ;

    private final Integer code;
    private final String name;

    public static CrmPharmacyTypeEnum getByCode(Integer code) {
        for (CrmPharmacyTypeEnum e : CrmPharmacyTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

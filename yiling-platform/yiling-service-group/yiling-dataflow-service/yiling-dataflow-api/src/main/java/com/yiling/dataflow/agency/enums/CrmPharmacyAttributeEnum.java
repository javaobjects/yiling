package com.yiling.dataflow.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 药店属性 1-连锁分店；2-单体药店
 *
 * @author: yong.zhang
 * @date: 2023/2/16 0016
 */
@Getter
@AllArgsConstructor
public enum CrmPharmacyAttributeEnum {
    /**
     * 1-连锁分店
     */
    CHAIN(1, "连锁分店"),
    /**
     * 2-单体药店
     */
    SIMPLE(2, "单体药店"),
    ;

    private final Integer code;
    private final String name;

    public static CrmPharmacyAttributeEnum getByCode(Integer code) {
        for (CrmPharmacyAttributeEnum e : CrmPharmacyAttributeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

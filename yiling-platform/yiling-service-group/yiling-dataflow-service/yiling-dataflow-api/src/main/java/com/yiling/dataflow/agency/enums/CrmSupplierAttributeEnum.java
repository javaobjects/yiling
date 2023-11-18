package com.yiling.dataflow.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商业属性 1-城市商业、2-县级商业
 */
@Getter
@AllArgsConstructor
public enum CrmSupplierAttributeEnum {
    /**
     * 城市商业
     */
    CITY_BUSINESS(1, "城市商业"),
    /**
     * 县级商业
     */
    COUNTY_BUSINESS(2, "县级商业"),
    ;

    private final Integer code;
    private final String name;

    public static CrmSupplierAttributeEnum getByCode(Integer code) {
        for (CrmSupplierAttributeEnum e : CrmSupplierAttributeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
    public static String getNameByCode(Integer code) {
        for (CrmSupplierAttributeEnum e : CrmSupplierAttributeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getName();
            }
        }
        return null;
    }
}

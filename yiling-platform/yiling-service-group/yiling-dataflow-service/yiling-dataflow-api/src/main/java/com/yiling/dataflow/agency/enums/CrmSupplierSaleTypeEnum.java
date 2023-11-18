package com.yiling.dataflow.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商业销售类型 填写分销型/纯销型/综合型
 */
@Getter
@AllArgsConstructor
public enum CrmSupplierSaleTypeEnum {
    /**
     * 分销型
     */
    DISTRIBUTION(1, "分销型"),
    /**
     * 纯销型
     */
    PURESALE(2, "纯销型"),
    /**
     * 综合型
     */
    COMPREHENSIVE(3, "综合型"),
    ;

    private final Integer code;
    private final String name;

    public static CrmSupplierSaleTypeEnum getByCode(Integer code) {
        for (CrmSupplierSaleTypeEnum e : CrmSupplierSaleTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
    public static String getNameByCode(Integer code) {
        for (CrmSupplierSaleTypeEnum e : CrmSupplierSaleTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getName();
            }
        }
        return null;
    }
}

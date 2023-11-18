package com.yiling.dataflow.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 基药商信息 1-基药配送商、2-基药促销商
 */
@Getter
@AllArgsConstructor
public enum CrmBaseSupplierInfoEnum {
    /**
     * 基药配送商
     */
    DISTRIBUTION(1, "基药配送商"),
    /**
     * 基药促销商
     */
    PROMOTION(2, "基药促销商"),
    ;

    private final Integer code;
    private final String name;

    public static CrmBaseSupplierInfoEnum getByCode(Integer code) {
        for (CrmBaseSupplierInfoEnum e : CrmBaseSupplierInfoEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
    public static String getNameByCode(Integer code) {
        for (CrmBaseSupplierInfoEnum e : CrmBaseSupplierInfoEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getName();
            }
        }
        return null;
    }
}

package com.yiling.dataflow.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 普药销售模式 1-分销模式、2-招商模式、3-底价承包模式、4-KA连锁模式
 */
@Getter
@AllArgsConstructor
public enum CrmGeneralMedicineSaleTypeEnum {
    /**
     * 分销模式
     */
    DISTRIBUTION(1, "分销模式"),
    /**
     * 招商模式
     */
    ATTRACT(2, "招商模式"),
    /**
     * 底价承包模式
     */
    BASEPRICE(3, "底价承包模式"),
    /**
     * KA连锁模式
     */
    CHAINKA(4, "KA连锁模式"),
    ;

    private final Integer code;
    private final String name;

    public static CrmGeneralMedicineSaleTypeEnum getByCode(Integer code) {
        for (CrmGeneralMedicineSaleTypeEnum e : CrmGeneralMedicineSaleTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
    public static String getNameByCode(Integer code) {
        for (CrmGeneralMedicineSaleTypeEnum e : CrmGeneralMedicineSaleTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getName();
            }
        }
        return null;
    }
}

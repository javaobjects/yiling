package com.yiling.dataflow.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否医保 1-医保药店；2-慢保药店；3-否医保
 *
 * @author: yong.zhang
 * @date: 2023/2/18 0018
 */
@Getter
@AllArgsConstructor
public enum CrmPharmacyMedicalInsuranceEnum {

    /**
     * 1-医保药店
     */
    MEDICAL_INSURANCE(1, "医保药店"),
    /**
     * 2-慢保药店
     */
    SLOW_MEDICAL_INSURANCE(2, "慢保药店"),
    /**
     * 3-否医保
     */
    NO_MEDICAL_INSURANCE(3, "否医保"),
    ;

    private final Integer code;
    private final String name;

    public static CrmPharmacyMedicalInsuranceEnum getByCode(Integer code) {
        for (CrmPharmacyMedicalInsuranceEnum e : CrmPharmacyMedicalInsuranceEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

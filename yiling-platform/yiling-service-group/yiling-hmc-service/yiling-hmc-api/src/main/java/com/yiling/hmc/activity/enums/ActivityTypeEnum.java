package com.yiling.hmc.activity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 活动类型
 *
 * @author: fan.shen
 * @date: 2023-01-13
 */
@Getter
@AllArgsConstructor
public enum ActivityTypeEnum {


    /**
     * 患教活动
     */
    PATIENT_EDUCATE(1, "患教活动"),

    /**
     * 医带患活动
     */
    DOC_TO_PATIENT(2, "医带患活动"),

    /**
     * 八子补肾活动
     */
    BA_ZI_BU_SHEN(3, "八子补肾活动"),

    ;

    private final Integer code;
    private final String name;

    /**
     * 是否医带患、八子补肾活动
     * @param code
     * @return
     */
    public static boolean isDocPatientOrBaZi(Integer code) {
        if (DOC_TO_PATIENT.getCode().equals(code) || BA_ZI_BU_SHEN.getCode().equals(code)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public static ActivityTypeEnum getByCode(Integer code) {
        for (ActivityTypeEnum e : ActivityTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

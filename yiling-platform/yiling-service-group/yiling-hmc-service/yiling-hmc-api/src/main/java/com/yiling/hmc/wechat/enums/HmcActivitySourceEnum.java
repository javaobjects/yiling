package com.yiling.hmc.wechat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * HMC活动来源枚举类
 *
 * @Author fan.shen
 * @Date 2023/3/28
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum HmcActivitySourceEnum {

    /**
     * 患教活动
     */
    PATIENT_EDUCATE(1, "患教活动"),

    /**
     * 医带患
     */
    DOC_TO_PATIENT(2, "医带患"),

    /**
     * 八子补肾
     */
    BA_ZI_BU_SHEN(3, "八子补肾"),

    ;

    private Integer type;

    private String name;

    /**
     * 校验是否新活动类型
     * 2-医带患、3-八子补肾
     *
     * @param type
     * @return
     */
    public static Boolean checkIsNewActivity(Integer type) {
        if (DOC_TO_PATIENT.getType().equals(type) || BA_ZI_BU_SHEN.getType().equals(type)) {
            return true;
        }
        return false;
    }

    public static HmcActivitySourceEnum getByType(Integer type) {
        for (HmcActivitySourceEnum e : HmcActivitySourceEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

}

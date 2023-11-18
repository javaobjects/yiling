package com.yiling.hmc.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 处方开方状态 1-已开，2-未开
 *
 * @author: fan.shen
 * @date: 2022/4/8
 */
@Getter
@AllArgsConstructor
public enum HmcPrescriptionStatusEnum {

    /**
     * 1-已开
     */
    HAVE_PRESCRIPTION(1, "已开"),

    /**
     * 2-未开
     */
    NOT_PRESCRIPTION(2, "未开")
    ;

    private final Integer code;
    private final String name;

    public static HmcPrescriptionStatusEnum getByCode(Integer code) {
        for (HmcPrescriptionStatusEnum e : HmcPrescriptionStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

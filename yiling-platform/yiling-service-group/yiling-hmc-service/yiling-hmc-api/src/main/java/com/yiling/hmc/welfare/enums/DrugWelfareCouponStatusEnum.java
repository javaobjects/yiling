package com.yiling.hmc.welfare.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 福利券状态 1-待激活，2-已激活，3-已使用
 *
 * @author: fan.shen
 * @date: 2022-09-27
 */
@Getter
@AllArgsConstructor
public enum DrugWelfareCouponStatusEnum {

    /**
     * 未激活
     */
    TO_ACTIVE(1, "未激活"),

    /**
     * 已激活
     */
    ACTIVATED(2, "已激活"),

    /**
     * 已核销
     */
    USED(3, "已核销"),


    ;

    private final Integer code;

    private final String name;

    public static DrugWelfareCouponStatusEnum getByCode(Integer code) {
        for (DrugWelfareCouponStatusEnum e : DrugWelfareCouponStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

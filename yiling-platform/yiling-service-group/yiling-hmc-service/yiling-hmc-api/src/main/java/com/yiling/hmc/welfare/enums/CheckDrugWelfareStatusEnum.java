package com.yiling.hmc.welfare.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 药品福利计划状态 1-未开始，2-已过期，3-已结束
 *
 * @author: fan.shen
 * @date: 2022-09-27
 */
@Getter
@AllArgsConstructor
public enum CheckDrugWelfareStatusEnum {

    /**
     * 未开始
     */
    NOT_START(1, "未开始"),

    /**
     * 已过期
     */
    EXPIRED(2, "已过期"),

    /**
     * 已结束
     */
    ENDED(3, "已结束"),

    ;

    private final Integer code;

    private final String name;

    public static CheckDrugWelfareStatusEnum getByCode(Integer code) {
        for (CheckDrugWelfareStatusEnum e : CheckDrugWelfareStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

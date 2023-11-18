package com.yiling.hmc.welfare.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 福利券状态 1-待激活，2-已激活，3-已使用
 *
 * @author: benben.jia
 * @date: 2022-10-14
 */
@Getter
@AllArgsConstructor
public enum DrugWelfareGroupCouponVerificationStatusEnum {

    /**
     * 券码输入错误
     */
    ERROR(1, "券码输入错误"),

    /**
     * 已经核销
     */
    VERIFICATION(2, "已经核销"),

    /**
     * 活动不存在
     */
    NON_EXIST(3, "活动不存在"),

    /**
     * 活动未开始
     */
    NOT_START(4, "活动未开始"),

    /**
     * 活动已过期
     */
    EXPIRED(5, "活动已过期"),

    /**
     * 活动已结束
     */
    ENDED(6, "活动已结束"),

    /**
     * 核销成功
     */
    SUCCESS(7, "核销成功"),


    ;

    private final Integer code;

    private final String name;

    public static DrugWelfareGroupCouponVerificationStatusEnum getByCode(Integer code) {
        for (DrugWelfareGroupCouponVerificationStatusEnum e : DrugWelfareGroupCouponVerificationStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

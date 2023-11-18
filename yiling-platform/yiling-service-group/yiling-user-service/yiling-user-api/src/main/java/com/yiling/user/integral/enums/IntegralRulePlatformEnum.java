package com.yiling.user.integral.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 积分发放/消耗规则平台枚举类
 *
 * @author: lun.yu
 * @date: 2022-12-29
 */
@Getter
@AllArgsConstructor
public enum IntegralRulePlatformEnum {

    /**
     * 平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手 （动态状态，字典：integral_rule_platform ，枚举：IntegralRulePlatformEnum）
     */
    B2B(1, "B2B"),
    HMC(2, "健康管理中心患者端"),
    DOCTOR(3, "以岭互联网医院医生端"),
    PHARMACY_ASSISTANT(4, "药店店员端"),
    MR(5, "医药代表端"),
    SALES_ASSISTANT(6, "销售助手"),
    ;

    private final Integer code;
    private final String name;

    public static IntegralRulePlatformEnum getByCode(Integer code) {
        for (IntegralRulePlatformEnum e : IntegralRulePlatformEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

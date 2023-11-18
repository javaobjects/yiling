package com.yiling.user.integral.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 积分规则适用平台枚举类
 *
 * @author: lun.yu
 * @date: 2022-12-30
 */
@Getter
@AllArgsConstructor
public enum IntegralRuleUsePlatformEnum {

    /**
     * 平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手 10-所有业务线通用 （动态状态，字典：integral_rule_platform ，枚举：IntegralRulePlatformEnum）
     */
    B2B(1, "B2B"),
    HMC(2, "健康管理中心患者端"),
    DOCTOR(3, "以岭互联网医院医生端"),
    PHARMACY_ASSISTANT(4, "药店店员端"),
    MR(5, "医药代表端"),
        SALES_ASSISTANT(6, "销售助手"),
    ALL_LINE(10, "所有业务线通用"),
    ;

    private final Integer code;
    private final String name;

    public static IntegralRuleUsePlatformEnum getByCode(Integer code) {
        for (IntegralRuleUsePlatformEnum e : IntegralRuleUsePlatformEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

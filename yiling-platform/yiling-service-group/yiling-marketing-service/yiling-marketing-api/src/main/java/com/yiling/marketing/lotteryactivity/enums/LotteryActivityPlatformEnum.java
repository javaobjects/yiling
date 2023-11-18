package com.yiling.marketing.lotteryactivity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 抽奖活动平台枚举类
 *
 * @author: lun.yu
 * @date: 2022-09-13
 */
@Getter
@AllArgsConstructor
public enum LotteryActivityPlatformEnum {

    /**
     * 活动平台：1-B2B 2-健康管理中心公众号 3-健康管理中心患者端 4-以岭互联网医院患者端 5-以岭互联网医院医生端 6-医药代表端 7-店员端
     */
    B2B(1, "B2B"),
    ZGH(2, "健康管理中心公众号"),
    ZX_HZD(3, "健康管理中心患者端"),
    YY_HZD(4, "以岭互联网医院患者端"),
    YY_YSD(5, "以岭互联网医院医生端"),
    YYDB(6, "医药代表端"),
    DYD(7, "店员端"),
    ;

    private final Integer code;
    private final String name;

    public static LotteryActivityPlatformEnum getByCode(Integer code) {
        for (LotteryActivityPlatformEnum e : LotteryActivityPlatformEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

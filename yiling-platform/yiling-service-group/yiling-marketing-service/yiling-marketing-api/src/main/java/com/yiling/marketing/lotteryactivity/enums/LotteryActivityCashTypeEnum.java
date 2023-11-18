package com.yiling.marketing.lotteryactivity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 抽奖活动兑付奖品类型枚举类
 *
 * @author: lun.yu
 * @date: 2022-09-01
 */
@Getter
@AllArgsConstructor
public enum LotteryActivityCashTypeEnum {

    /**
     * 抽奖活动兑付奖品类型枚举类
     */
    ONE(1, "单个兑付"),
    CURRENT(2, "当前页兑付"),
    ALL(3, "兑付全部"),
    ;

    private final Integer code;
    private final String name;

    public static LotteryActivityCashTypeEnum getByCode(Integer code) {
        for (LotteryActivityCashTypeEnum e : LotteryActivityCashTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

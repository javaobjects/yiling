package com.yiling.marketing.lotteryactivity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 抽奖活动赠送范围枚举类
 *
 * @author: lun.yu
 * @date: 2022-08-30
 */
@Getter
@AllArgsConstructor
public enum LotteryActivityGiveScopeEnum {

    /**
     * 赠送范围：1-全部客户 2-指定客户 3-指定范围客户
     */
    ALL_CUSTOMER(1, "全部客户"),
    ASSIGN_CUSTOMER(2, "指定客户"),
    ASSIGN_SCOPE_CUSTOMER(3, "指定范围客户"),
    ;

    private final Integer code;
    private final String name;

    public static LotteryActivityGiveScopeEnum getByCode(Integer code) {
        for (LotteryActivityGiveScopeEnum e : LotteryActivityGiveScopeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

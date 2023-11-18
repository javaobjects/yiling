package com.yiling.marketing.lotteryactivity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 抽奖活动奖品类型枚举类
 *
 * @author: lun.yu
 * @date: 2022-08-30
 */
@Getter
@AllArgsConstructor
public enum LotteryActivityGiveUserTypeEnum {

    /**
     * 指定范围客户用户类型：1-全部用户 2-普通用户 3-全部会员 4-指定方案会员 5-指定推广方会员
     */
    ALL_USER(1, "全部用户"),
    NORMAL_USER(2, "普通用户"),
    ALL_MEMBER(3, "全部会员"),
    ASSIGN_MEMBER(4, "指定方案会员"),
    ASSIGN_PROMOTER_MEMBER(5, "指定推广方会员"),
    ;

    private final Integer code;
    private final String name;

    public static LotteryActivityGiveUserTypeEnum getByCode(Integer code) {
        for (LotteryActivityGiveUserTypeEnum e : LotteryActivityGiveUserTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

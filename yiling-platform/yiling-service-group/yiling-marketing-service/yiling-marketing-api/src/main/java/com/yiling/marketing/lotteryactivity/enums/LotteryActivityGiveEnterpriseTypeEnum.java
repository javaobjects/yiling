package com.yiling.marketing.lotteryactivity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 抽奖活动赠送企业类型枚举类
 *
 * @author: lun.yu
 * @date: 2022-08-30
 */
@Getter
@AllArgsConstructor
public enum LotteryActivityGiveEnterpriseTypeEnum {

    /**
     * 指定范围客户企业类型：1-全部类型 2-指定类型
     */
    ALL_TYPE(1, "全部类型"),
    ASSIGN_TYPE(2, "指定类型"),
    ;

    private final Integer code;
    private final String name;

    public static LotteryActivityGiveEnterpriseTypeEnum getByCode(Integer code) {
        for (LotteryActivityGiveEnterpriseTypeEnum e : LotteryActivityGiveEnterpriseTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

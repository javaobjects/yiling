package com.yiling.marketing.lotteryactivity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 抽奖活动状态枚举
 *
 * @author: lun.yu
 * @date: 2022-10-31
 */
@Getter
@AllArgsConstructor
public enum LotteryActivityStatusEnum {

    ENABLED(1, "启用"),
    DISABLED(2, "停用"),
    DRAFT(3, "草稿"),
    ;

    private final Integer code;
    private final String name;

    public static LotteryActivityStatusEnum getByCode(Integer code) {
        for (LotteryActivityStatusEnum e : LotteryActivityStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

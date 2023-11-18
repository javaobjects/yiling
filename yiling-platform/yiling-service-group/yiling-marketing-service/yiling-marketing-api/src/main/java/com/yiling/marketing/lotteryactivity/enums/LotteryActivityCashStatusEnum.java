package com.yiling.marketing.lotteryactivity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 抽奖活动兑付状态枚举类
 *
 * @author: lun.yu
 * @date: 2022-09-01
 */
@Getter
@AllArgsConstructor
public enum LotteryActivityCashStatusEnum {

    /**
     * 兑付状态：1-已兑付 2-未兑付
     */
    HAD_CASH(1, "已兑付"),
    UN_CASH(2, "未兑付"),
    ;

    private final Integer code;
    private final String name;

    public static LotteryActivityCashStatusEnum getByCode(Integer code) {
        for (LotteryActivityCashStatusEnum e : LotteryActivityCashStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

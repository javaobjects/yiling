package com.yiling.marketing.lotteryactivity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 抽奖活动进度枚举类
 *
 * @author: lun.yu
 * @date: 2022-08-29
 */
@Getter
@AllArgsConstructor
public enum LotteryActivityProgressEnum {

    /**
     * 活动进度：1-未开始 2-进行中 3-已结束 （动态状态，枚举：lottery_activity_progress ，字典：LotteryActivityProgressEnums）
     */
    UNDO(1, "未开始"),
    GOING(2, "进行中"),
    END(3, "已结束"),
    ;

    private final Integer code;
    private final String name;

    public static LotteryActivityProgressEnum getByCode(Integer code) {
        for (LotteryActivityProgressEnum e : LotteryActivityProgressEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

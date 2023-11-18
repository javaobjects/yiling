package com.yiling.marketing.lotteryactivity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 抽奖活动重复执行赠送枚举类
 *
 * @author: lun.yu
 * @date: 2022-11-04
 */
@Getter
@AllArgsConstructor
public enum LotteryActivityLoopGiveEnum {

    /**
     * 重复执行：1-关闭 2-每天重复执行
     */
    OFF(1, "关闭"),
    EVERY_GIVE(2, "每天重复执行"),
    ;

    private final Integer code;
    private final String name;

    public static LotteryActivityLoopGiveEnum getByCode(Integer code) {
        for (LotteryActivityLoopGiveEnum e : LotteryActivityLoopGiveEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

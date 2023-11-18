package com.yiling.marketing.lotteryactivity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 抽奖活动获取方式枚举类
 *
 * @author: lun.yu
 * @date: 2022-09-14
 */
@Getter
@AllArgsConstructor
public enum LotteryActivityGetTypeEnum {

    /**
     * 获取方式：1-签到、2-抽奖赠送、3-活动每天赠送、4-分享、5-活动开始赠送、6-购买会员、7-订单累计金额赠送、8-时间周期、9-积分兑换
     */
    SIGN(1, "签到"),
    DRAW_GIVE(2, "抽奖赠送"),
    EVERY(3, "活动每天赠送"),
    SHARE(4, "分享"),
    START_GIVE(5, "活动开始赠送"),
    BUY_MEMBER(6, "购买会员"),
    ORDER_GIVE(7, "订单累计金额赠送"),
    TIME_CIRCLE(8, "时间周期"),
    INTEGRAL(9,"积分兑换"),
    ;

    private final Integer code;
    private final String name;

    public static LotteryActivityGetTypeEnum getByCode(Integer code) {
        for (LotteryActivityGetTypeEnum e : LotteryActivityGetTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

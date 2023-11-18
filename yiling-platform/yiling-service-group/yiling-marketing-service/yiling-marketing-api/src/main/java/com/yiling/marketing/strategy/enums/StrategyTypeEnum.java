package com.yiling.marketing.strategy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 策略类型：1-订单累计金额/2-签到天数/3-时间周期/4-购买会员
 *
 * @author: yong.zhang
 * @date: 2022/8/24
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum StrategyTypeEnum {

    /**
     * 订单累计金额
     */
    ORDER_AMOUNT(1, "订单累计金额"),
    /**
     * 签到天数
     */
    SIGN_DAYS(2, "签到天数"),
    /**
     * 时间周期
     */
    CYCLE_TIME(3, "时间周期"),
    /**
     * 购买会员
     */
    PURCHASE_MEMBER(4, "购买会员"),
    ;

    private Integer type;
    private String name;

    public static StrategyTypeEnum getByType(Integer type) {
        for (StrategyTypeEnum e : StrategyTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }
}

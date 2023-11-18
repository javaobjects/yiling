package com.yiling.user.integral.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 积分发放规则进度枚举类
 *
 * @author: lun.yu
 * @date: 2022-12-29
 */
@Getter
@AllArgsConstructor
public enum IntegralGiveRuleProgressEnum {

    /**
     * 活动进度：1-未开始 2-进行中 3-已结束 （动态状态，字典：integral_give_rule_progress ，枚举：IntegralGiveRuleProgressEnum）
     */
    UNDO(1, "未开始"),
    GOING(2, "进行中"),
    END(3, "已结束"),
    ;

    private final Integer code;
    private final String name;

    public static IntegralGiveRuleProgressEnum getByCode(Integer code) {
        for (IntegralGiveRuleProgressEnum e : IntegralGiveRuleProgressEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

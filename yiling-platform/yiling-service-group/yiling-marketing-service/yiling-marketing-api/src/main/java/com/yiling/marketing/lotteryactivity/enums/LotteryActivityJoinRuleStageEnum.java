package com.yiling.marketing.lotteryactivity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 抽奖活动C端 活动参与门槛枚举类
 *
 * @author: lun.yu
 * @date: 2022-09-16
 */
@Getter
@AllArgsConstructor
public enum LotteryActivityJoinRuleStageEnum {

    /**
     * 抽奖活动C端 活动参与门槛：1-不限制 2-关注健康管理中心公众号
     */
    NONE(1, "不限制"),
    SUBSCRIBE(2, "关注健康管理中心公众号"),
    ;

    private final Integer code;
    private final String name;

    public static LotteryActivityJoinRuleStageEnum getByCode(Integer code) {
        for (LotteryActivityJoinRuleStageEnum e : LotteryActivityJoinRuleStageEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

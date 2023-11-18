package com.yiling.user.integral.enums;

import java.util.List;

import cn.hutool.core.collection.ListUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 积分行为枚举类
 *
 * @author: lun.yu
 * @date: 2023-01-03
 */
@Getter
@AllArgsConstructor
public enum IntegralBehaviorEnum {

    /**
     * 积分规则类型：1-发放 2-消耗
     */
    SIGN_GIVE_INTEGRAL("签到送积分", 1, ListUtil.toList(IntegralRuleUsePlatformEnum.ALL_LINE)),
    ORDER_GIVE_INTEGRAL("订单送积分", 1, ListUtil.toList(IntegralRuleUsePlatformEnum.B2B)),
    JOIN_ACTIVITY_USE("参与活动消耗", 2, ListUtil.toList(IntegralRuleUsePlatformEnum.B2B, IntegralRuleUsePlatformEnum.HMC)),
    EXCHANGE_USE("兑换消耗", 2, ListUtil.toList(IntegralRuleUsePlatformEnum.ALL_LINE)),
    REFUND_REDUCE("退货扣减", 2, ListUtil.toList(IntegralRuleUsePlatformEnum.B2B)),
    EXPIRED_INVALID("过期作废", 2, ListUtil.toList(IntegralRuleUsePlatformEnum.B2B)),
    DIRECTIONAL_GIVE("定向赠送", 1, ListUtil.toList(IntegralRuleUsePlatformEnum.ALL_LINE)),
    ;

    private final String name;
    private final Integer type;
    private final List<IntegralRuleUsePlatformEnum> list;

    public static IntegralBehaviorEnum getByName(String name) {
        for (IntegralBehaviorEnum e : IntegralBehaviorEnum.values()) {
            if (e.getName().equals(name)) {
                return e;
            }
        }
        return null;
    }
}

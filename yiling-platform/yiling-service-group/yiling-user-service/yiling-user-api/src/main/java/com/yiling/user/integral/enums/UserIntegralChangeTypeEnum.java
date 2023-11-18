package com.yiling.user.integral.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户积分变更类型枚举类
 *
 * @author: lun.yu
 * @date: 2023-01-13
 */
@Getter
@AllArgsConstructor
public enum UserIntegralChangeTypeEnum {

    /**
     * 用户积分变更类型：1-订单送积分 2-签到送积分 3-参与活动消耗 4-兑换消耗 5-退货扣减 6-过期作废
     */
    ORDER_GIVE_INTEGRAL(1, "订单送积分"),
    SIGN_GIVE_INTEGRAL(2, "签到送积分"),
    JOIN_ACTIVITY_USE(3, "参与活动消耗"),
    EXCHANGE_USE(4, "兑换消耗"),
    REFUND_REDUCE(5, "退货扣减"),
    EXPIRED_INVALID(6, "过期作废"),
    DIRECTIONAL_GIVE(7, "定向赠送"),
    ;

    private final Integer code;
    private final String name;

    public static UserIntegralChangeTypeEnum getByCode(Integer code) {
        for (UserIntegralChangeTypeEnum e : UserIntegralChangeTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

package com.yiling.user.integral.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 积分兑付订单状态枚举类
 *
 * @author: lun.yu
 * @date: 2023-01-12
 */
@Getter
@AllArgsConstructor
public enum IntegralExchangeOrderStatusEnum {

    /**
     * 兑换状态：1-未兑换 2-已兑换
     */
    UN_CASH(1, "未兑付"),
    HAD_CASH(2, "已兑付"),
    ;

    private final Integer code;
    private final String name;

    public static IntegralExchangeOrderStatusEnum getByCode(Integer code) {
        for (IntegralExchangeOrderStatusEnum e : IntegralExchangeOrderStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

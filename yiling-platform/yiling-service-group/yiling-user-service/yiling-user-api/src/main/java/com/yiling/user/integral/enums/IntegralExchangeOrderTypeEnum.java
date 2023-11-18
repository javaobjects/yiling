package com.yiling.user.integral.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 积分订单兑换类型枚举
 *
 * @author: lun.yu
 * @date: 2023-01-11
 */
@Getter
@AllArgsConstructor
public enum IntegralExchangeOrderTypeEnum {

    /**
     * 兑付类型：1-兑付全部 2-兑付当前页 3-单个兑付
     */
    ALL(1, "全部兑付"),
    CURRENT(2, "兑付当前页"),
    SINGLE(3, "单个兑付"),
    ;

    private final Integer code;
    private final String name;

    public static IntegralExchangeOrderTypeEnum getByCode(Integer code) {
        for (IntegralExchangeOrderTypeEnum e : IntegralExchangeOrderTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

package com.yiling.user.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 积分类型枚举
 *
 * @author: lun.yu
 * @date: 2021/10/20
 */
@Getter
@AllArgsConstructor
public enum IntegralTypeEnum {

    /**
     * 积分类型
     */
    ORDER_GET(1, "订单送积分"),
    ;

    private final Integer code;
    private final String name;

    public static IntegralTypeEnum getByCode(Integer code) {
        for (IntegralTypeEnum e : IntegralTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

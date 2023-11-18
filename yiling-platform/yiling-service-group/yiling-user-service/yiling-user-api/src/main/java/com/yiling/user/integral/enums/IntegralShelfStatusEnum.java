package com.yiling.user.integral.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 积分兑换商品上下架状态枚举
 *
 * @author: lun.yu
 * @date: 2023-01-09
 */
@Getter
@AllArgsConstructor
public enum IntegralShelfStatusEnum {

    // 积分兑换商品上下架状态：1-已上架 2-已下架
    SHELF(1, "已上架"),
    SOLD_OUT(2, "已下架"),
    ;

    private final Integer code;
    private final String name;

    public static IntegralShelfStatusEnum getByCode(Integer code) {
        for (IntegralShelfStatusEnum e : IntegralShelfStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

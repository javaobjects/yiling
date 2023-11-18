package com.yiling.hmc.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付状态:1-未支付/2-已支付/3-已退款/4-部分退款
 *
 * @author: fan.shen
 * @date: 2022/4/7
 */
@Getter
@AllArgsConstructor
public enum HmcPaymentStatusEnum {

    UN_PAY(1, "未支付"),

    PAYED(2, "已支付"),

    REFUND(3, "已全部退款"),

    PART_REFUND(4, "部分退款"),
    ;

    private final Integer code;
    private final String name;

    public static HmcPaymentStatusEnum getByCode(Integer code) {
        for (HmcPaymentStatusEnum e : HmcPaymentStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

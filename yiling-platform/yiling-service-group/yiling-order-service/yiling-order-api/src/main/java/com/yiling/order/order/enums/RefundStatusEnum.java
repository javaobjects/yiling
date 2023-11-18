package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 退款状态
 *
 * @author: yong.zhang
 * @date: 2021/11/23
 */
@Getter
@AllArgsConstructor
public enum RefundStatusEnum {
    // 退款状态1-待退款 2-退款中 3-退款成功 4-退款失败
    UN_REFUND(1, "待退款"),
    REFUNDING(2, "退款中"),
    REFUNDED(3, "退款成功"),
    REFUNDED_FAIL(4, "退款失败"),
    ;

    private Integer code;
    private String  name;

    public static RefundStatusEnum getByCode(Integer code) {
        for (RefundStatusEnum e : RefundStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

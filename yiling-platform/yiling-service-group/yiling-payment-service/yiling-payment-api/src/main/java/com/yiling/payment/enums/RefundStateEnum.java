package com.yiling.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.enums
 * @date: 2021/10/29
 */
@Getter
@AllArgsConstructor
public enum  RefundStateEnum {

    WAIT_REFUND(1, "待退款"),
    REFUND_ING(2, "退款中"),
    FALIUE(3, "退款失败"),
    SUCCESS(4, "退款成功"),
    CLOSE(5, "退款关闭"),
    ;

    private Integer code;
    private String name;

    public static RefundStateEnum getByCode(Integer code) {
        for (RefundStateEnum e : RefundStateEnum.values()) {

            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

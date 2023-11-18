package com.yiling.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.enmums
 * @date: 2021/10/21
 */
@Getter
@AllArgsConstructor
public enum AppOrderStatusEnum {

    WAIT_PAY(1, "待支付"),
    PAY_ING(2, "支付中"),
    SUCCESS(3, "支付成功"),
    CLOSE(4, "交易取消"),
    ;

    private Integer code;
    private String name;

    public static AppOrderStatusEnum getByCode(Integer code) {
        for (AppOrderStatusEnum e : AppOrderStatusEnum.values()) {

            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

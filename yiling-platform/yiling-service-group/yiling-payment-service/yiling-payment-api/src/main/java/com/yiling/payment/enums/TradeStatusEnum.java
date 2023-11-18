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
public enum TradeStatusEnum {

    WAIT_PAY(1, "待支付"),
    SUCCESS(2, "交易成功"),
    CLOSE(3, "交易取消"),
    FALIUE(4, "付款失败"),
    BANK_ING(5, "银行处理中")
    ;




    private Integer code;
    private String name;

    public static TradeStatusEnum getByCode(Integer code) {
        for (TradeStatusEnum e : TradeStatusEnum.values()) {

            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

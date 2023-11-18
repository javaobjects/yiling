package com.yiling.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dexi.yao
 * @date 2021-11-08
 */
@Getter
@AllArgsConstructor
public enum YeePayEnum {

	//支付
    PAY(1, "支付"),
	//退款
	REFUND(2, "退款"),
	;

    private Integer code;
    private String name;

    public static YeePayEnum getByCode(Integer code) {
        for (YeePayEnum e : YeePayEnum.values()) {

            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

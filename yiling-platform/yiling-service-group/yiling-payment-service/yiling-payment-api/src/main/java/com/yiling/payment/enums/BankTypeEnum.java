package com.yiling.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 银行类型 1-总行 2-支行
 * @author dexi.yao
 * @date 2021-11-08
 */
@Getter
@AllArgsConstructor
public enum BankTypeEnum {

	//总行
    HEADQUARTERS(1, "总行"),
	//支行
	BRANCH(2, "支行"),
	;

    private Integer code;
    private String name;

    public static BankTypeEnum getByCode(Integer code) {
        for (BankTypeEnum e : BankTypeEnum.values()) {

            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

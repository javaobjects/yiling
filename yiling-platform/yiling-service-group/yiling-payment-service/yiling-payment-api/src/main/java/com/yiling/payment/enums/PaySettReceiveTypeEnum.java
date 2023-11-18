package com.yiling.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 结算单付款到账类型枚举
 *
 * @author dexi.yao
 * @date 2021-11-12
 */
@Getter
@AllArgsConstructor
public enum PaySettReceiveTypeEnum {

	//实时
	SHORTLY(1, "实时"),
	//两小时到账
	FEW_HOUR(2, "两小时到账"),
	//次日到账
	NEXT_DAY(3, "次日到账"),
	;

	private Integer code;
	private String  name;

	public static PaySettReceiveTypeEnum getByCode(Integer code) {
		for (PaySettReceiveTypeEnum e : PaySettReceiveTypeEnum.values()) {

			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

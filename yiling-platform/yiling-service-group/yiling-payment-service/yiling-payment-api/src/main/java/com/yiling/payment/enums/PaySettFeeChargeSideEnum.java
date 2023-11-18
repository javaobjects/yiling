package com.yiling.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 结算单付款手续费承担方枚举
 *
 * @author dexi.yao
 * @date 2021-11-12
 */
@Getter
@AllArgsConstructor
public enum PaySettFeeChargeSideEnum {

	//平台
	PLATFORM(1, "平台"),
	//用户
	USER(2, "用户"),
	;

	private Integer code;
	private String  name;

	public static PaySettFeeChargeSideEnum getByCode(Integer code) {
		for (PaySettFeeChargeSideEnum e : PaySettFeeChargeSideEnum.values()) {

			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

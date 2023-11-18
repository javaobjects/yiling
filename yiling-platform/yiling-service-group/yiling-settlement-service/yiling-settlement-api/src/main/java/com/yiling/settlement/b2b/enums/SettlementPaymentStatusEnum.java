package com.yiling.settlement.b2b.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 结算状态枚举
 *
 * @author: dexi.yao
 * @date: 2021/10/21
 */
@Getter
@AllArgsConstructor
public enum SettlementPaymentStatusEnum {
	//成功
	SUCCESS(1, "成功"),
	//失败
	FAIL(2, "失败"),
	;

	private Integer code;
	private String  name;

	public static SettlementPaymentStatusEnum getByCode(Integer code) {
		for (SettlementPaymentStatusEnum e : SettlementPaymentStatusEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

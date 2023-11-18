package com.yiling.settlement.b2b.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 企业收款账户失效状态枚举
 *
 * @author: dexi.yao
 * @date: 2021/10/27
 */
@Getter
@AllArgsConstructor
public enum ReceiptAccountValidEnum {
	//有效
	VALID(1, "有效"),
	//失效
	INVALID(2, "失效");

	private Integer code;
	private String  name;

	public static ReceiptAccountValidEnum getByCode(Integer code) {
		for (ReceiptAccountValidEnum e : ReceiptAccountValidEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

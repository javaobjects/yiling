package com.yiling.settlement.b2b.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 企业收款账户状态枚举
 *
 * @author: dexi.yao
 * @date: 2021/10/27
 */
@Getter
@AllArgsConstructor
public enum ReceiptAccountStatusEnum {
	//待审核
	WAIT(1, "待审核"),
	//审核成功
	SUCCESS(2, "审核成功"),
	//审核失败
	FAIL(3, "审核失败");

	private Integer code;
	private String  name;

	public static ReceiptAccountStatusEnum getByCode(Integer code) {
		for (ReceiptAccountStatusEnum e : ReceiptAccountStatusEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

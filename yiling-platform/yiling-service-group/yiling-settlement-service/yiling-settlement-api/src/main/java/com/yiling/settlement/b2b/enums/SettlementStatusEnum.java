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
public enum SettlementStatusEnum {
	//待结算
	UN_SETTLE(1, "待结算"),
	//银行处理中
	BANK_COPE(2, "银行处理中"),
	//结算成功
	SETTLE(3, "结算成功"),
	//结算失败
	FAIL(4, "结算失败");

	private Integer code;
	private String  name;

	public static SettlementStatusEnum getByCode(Integer code) {
		for (SettlementStatusEnum e : SettlementStatusEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

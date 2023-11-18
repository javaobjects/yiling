package com.yiling.settlement.b2b.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 结算单操作类型枚举
 *
 * @author: dexi.yao
 * @date: 2021/10/21
 */
@Getter
@AllArgsConstructor
public enum SettlementOperationTypeEnum {
	//发起打款时调用
	SEND(1, "发起打款时调用"),
	//打款回调时调用
	CALL(2, "打款回调时调用");

	private Integer code;
	private String  name;

	public static SettlementOperationTypeEnum getByCode(Integer code) {
		for (SettlementOperationTypeEnum e : SettlementOperationTypeEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

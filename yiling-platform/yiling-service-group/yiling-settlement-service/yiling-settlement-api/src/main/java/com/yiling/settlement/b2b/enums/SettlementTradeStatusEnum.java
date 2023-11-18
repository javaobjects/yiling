package com.yiling.settlement.b2b.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: dexi.yao
 * @date: 2021/10/21
 */
@Getter
@AllArgsConstructor
public enum SettlementTradeStatusEnum {

	//待支付
	WAIT_PAY(1, "待支付"),
	//交易成功
	SUCCESS(2, "交易成功"),
	//交易取消
	CLOSE(3, "交易取消"),
	//付款失败
	FALIUE(4, "付款失败"),
	//银行处理中
	BANK_ING(5, "银行处理中");


	private Integer code;
	private String  name;

	public static SettlementTradeStatusEnum getByCode(Integer code) {
		for (SettlementTradeStatusEnum e : SettlementTradeStatusEnum.values()) {

			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

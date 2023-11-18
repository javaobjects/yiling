package com.yiling.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 结算单付款表状态枚举 交易状态：1-待支付 2-银行处理中 3-打款成功 4-打款失败 5-已撤销
 *
 * @author dexi.yao
 * @date 2021-11-08
 */
@Getter
@AllArgsConstructor
public enum PaymentSettlementStatusEnum {

	//待支付
	WAIT_PAY(1, "待支付"),
	//银行处理中
	BANK_COPE(2, "银行处理中"),
	//打款成功
	SUCCESS(3, "打款成功"),
	//打款失败
	FAIL(4, "打款失败"),
	//已撤销
	CHANCE(5, "已撤销");


	private Integer code;
	private String  name;

	public static PaymentSettlementStatusEnum getByCode(Integer code) {
		for (PaymentSettlementStatusEnum e : PaymentSettlementStatusEnum.values()) {

			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

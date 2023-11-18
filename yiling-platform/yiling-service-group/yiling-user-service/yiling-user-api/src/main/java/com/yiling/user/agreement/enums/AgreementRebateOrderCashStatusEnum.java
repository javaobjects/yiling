package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 兑付状态1计算状态2已经兑付
 * @author dexi.yao
 * @date 2021-07-14
 */
@Getter
@AllArgsConstructor
public enum AgreementRebateOrderCashStatusEnum {

	//计算状态
	CALCULATE(1, "计算状态"),
	//已经兑付
	PAY(2, "已经兑付");

	private Integer code;

	private String name;

	public static AgreementRebateOrderCashStatusEnum getByCode(Integer code) {
		for (AgreementRebateOrderCashStatusEnum e : AgreementRebateOrderCashStatusEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

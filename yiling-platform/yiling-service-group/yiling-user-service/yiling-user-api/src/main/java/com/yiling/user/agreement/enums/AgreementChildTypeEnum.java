package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dexi.yao
 * @date 2021-06-23
 */
@Getter
@AllArgsConstructor
public enum AgreementChildTypeEnum {

	//购进额
	PURCHASE_AMOUNT(1, "购进额"),
	//购进量
	PURCHASE_CAPACITY(2, "购进量"),
	//回款额
	PAY_BACK_AMOUNT(3, "回款额"),
	//数据直连
	DATA_LINK(4, "数据直连"),
	//破损返利
	DAMAGED_REBATE(5, "破损返利");

	private Integer code;

	private String name;

	public static AgreementChildTypeEnum getByCode(Integer code) {
		for (AgreementChildTypeEnum e : AgreementChildTypeEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

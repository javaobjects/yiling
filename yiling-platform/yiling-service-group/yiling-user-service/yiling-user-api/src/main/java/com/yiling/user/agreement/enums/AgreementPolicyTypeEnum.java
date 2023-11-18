package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dexi.yao
 * @date 2021-06-23
 */
@Getter
@AllArgsConstructor
public enum AgreementPolicyTypeEnum {

	//购进额
	PURCHASE_AMOUNT_TYPE(1, "购进额"),
	//回款额
	BACK_AMOUNT_TYPE(2, "回款额"),;

	private Integer code;

	private String name;

	public static AgreementPolicyTypeEnum getByCode(Integer code) {
		for (AgreementPolicyTypeEnum e : AgreementPolicyTypeEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

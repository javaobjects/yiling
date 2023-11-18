package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dexi.yao
 * @date 2021-06-07
 */
@Getter
@AllArgsConstructor
public enum AgreementModeEnum {
	//双方协议
	SECOND_AGREEMENTS(1, "双方协议"),
	//三方协议
	THIRD_AGREEMENTS(2, "三方协议");

	private Integer code;

	private String name;

	public static AgreementModeEnum getByCode(Integer code) {
		for (AgreementModeEnum e : AgreementModeEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

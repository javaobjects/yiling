package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色 1-乙方 2-丙方
 * @author dexi.yao
 * @date 2021-06-11
 */
@Getter
@AllArgsConstructor
public enum AgreementRoleEnum {

	//乙方
	SECOND(1, "乙方"),
	//丙方
	THIRD(2, "丙方");

	private Integer code;

	private String name;

	public static AgreementRoleEnum getByCode(Integer code) {
		for (AgreementRoleEnum e : AgreementRoleEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

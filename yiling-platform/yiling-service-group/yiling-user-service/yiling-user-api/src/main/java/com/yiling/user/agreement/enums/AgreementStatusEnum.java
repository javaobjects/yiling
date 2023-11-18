package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态：1-启用 2-停用
 * @author dexi.yao
 * @date 2021-06-08
 */
@Getter
@AllArgsConstructor
public enum AgreementStatusEnum {

	//启用
	OPEN(1, "启用"),
	//停用
	CLOSE(2, "停用");

	private Integer code;

	private String name;

	public static AgreementStatusEnum getByCode(Integer code) {
		for (AgreementStatusEnum e : AgreementStatusEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

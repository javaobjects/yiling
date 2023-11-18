package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状操作类型 1-删除 2-停用
 * @author dexi.yao
 * @date 2021-06-08
 */
@Getter
@AllArgsConstructor
public enum AgreementCloseEnum {

	//启用
	DELETE(1, "删除"),
	//停用
	CLOSE(2, "停用");

	private Integer code;

	private String name;

	public static AgreementCloseEnum getByCode(Integer code) {
		for (AgreementCloseEnum e : AgreementCloseEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

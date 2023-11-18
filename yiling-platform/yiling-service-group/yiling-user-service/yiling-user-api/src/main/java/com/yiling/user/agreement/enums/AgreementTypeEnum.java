package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议类型 1-采购类 2-其他
 * @author dexi.yao
 * @date 2021-06-23
 */
@Getter
@AllArgsConstructor
public enum AgreementTypeEnum {

	//主分类-采购类
	PURCHASE(1, "采购类"),

	//主分类-其他
	OTHER(2, "其他");

	private Integer code;

	private String name;

	public static AgreementTypeEnum getByCode(Integer code) {
		for (AgreementTypeEnum e : AgreementTypeEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

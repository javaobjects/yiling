package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否已经满足条件状态1返利种类科目2归属执行部门
 * @author dexi.yao
 * @date 2021-07-14
 */
@Getter
@AllArgsConstructor
public enum AgreementRebateDictionariesStatusEnum {

	//返利种类科目
	SUBJECT(1, "返利种类科目"),
	//归属执行部门
	DEPT(2, "归属执行部门");

	private Integer code;

	private String name;

	public static AgreementRebateDictionariesStatusEnum getByCode(Integer code) {
		for (AgreementRebateDictionariesStatusEnum e : AgreementRebateDictionariesStatusEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

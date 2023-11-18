package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否已经满足条件状态1未满足2已经满足
 * @author dexi.yao
 * @date 2021-07-14
 */
@Getter
@AllArgsConstructor
public enum AgreementRebateOrderConditionStatusEnum {

	//未满足
	UN_PASS(1, "未满足"),
	//已经满足
	PASS(2, "已经满足");

	private Integer code;

	private String name;

	public static AgreementRebateOrderConditionStatusEnum getByCode(Integer code) {
		for (AgreementRebateOrderConditionStatusEnum e : AgreementRebateOrderConditionStatusEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

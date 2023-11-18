package com.yiling.sales.assistant.commissions.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 兑付状态 1-未兑付 2-以兑付
 *
 * @author dexi.yao
 * @date 2021-09-18
 */
@Getter
@AllArgsConstructor
public enum CommissionsDetailStatusEnum {
	//未兑付
	UN_SETTLEMENT(1, "未兑付"),
	//以兑付
	SETTLEMENT(2, "以兑付");

	private Integer code;

	private String name;

	public static CommissionsDetailStatusEnum getByCode(Integer code) {
		for (CommissionsDetailStatusEnum e : CommissionsDetailStatusEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

package com.yiling.sales.assistant.commissions.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 结算状态 1-未结清 2-已结清
 *
 * @author dexi.yao
 * @date 2021-09-18
 */
@Getter
@AllArgsConstructor
public enum CommissionsStatusEnum {
	//未结清
	UN_SETTLEMENT(1, "未结清"),
	//已结清
	SETTLEMENT(2, "已结清");

	private Integer code;

	private String name;

	public static CommissionsStatusEnum getByCode(Integer code) {
		for (CommissionsStatusEnum e : CommissionsStatusEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

package com.yiling.sales.assistant.commissions.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 佣金来源 1-任务收益 2-下线推广
 *
 * @author dexi.yao
 * @date 2021-09-18
 */
@Getter
@AllArgsConstructor
public enum CommissionsSourcesEnum {
	//任务收益
	TASK(1, "任务收益"),
	//下线收益
	SUBORDINATE(2, "下线收益");

	private Integer code;

	private String name;

	public static CommissionsSourcesEnum getByCode(Integer code) {
		for (CommissionsSourcesEnum e : CommissionsSourcesEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

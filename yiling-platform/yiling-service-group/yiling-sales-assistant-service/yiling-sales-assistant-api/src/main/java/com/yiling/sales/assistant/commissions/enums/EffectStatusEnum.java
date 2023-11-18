package com.yiling.sales.assistant.commissions.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 佣金是否有效 1-无效 2-有效
 *
 * @author dexi.yao
 * @date 2021-09-18
 */
@Getter
@AllArgsConstructor
public enum EffectStatusEnum {
	//无效
	INVALID(1, "无效"),
	//有效
	VALID(2, "有效");

	private Integer code;

	private String name;

	public static EffectStatusEnum getByCode(Integer code) {
		for (EffectStatusEnum e : EffectStatusEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

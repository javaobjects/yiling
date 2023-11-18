package com.yiling.sales.assistant.commissions.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 佣金类型 1-入账 2-出账
 *
 * @author dexi.yao
 * @date 2021-09-18
 */
@Getter
@AllArgsConstructor
public enum CommissionsTypeEnum {
	//入账
	INPUT(1, "入账"),
	//出账
	OUTPUT(2, "出账");

	private Integer code;

	private String name;

	public static CommissionsTypeEnum getByCode(Integer code) {
		for (CommissionsTypeEnum e : CommissionsTypeEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返利申请明细类型 1-协议返利 2-其他
 * @author dexi.yao
 * @date 2021-06-23
 */
@Getter
@AllArgsConstructor
public enum ApplyDetailTypeEnum {

	//协议返利
	AGREEMENT(1, "协议返利"),

	//其他
	OTHER(2, "其他");

	private Integer code;

	private String name;

	public static ApplyDetailTypeEnum getByCode(Integer code) {
		for (ApplyDetailTypeEnum e : ApplyDetailTypeEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

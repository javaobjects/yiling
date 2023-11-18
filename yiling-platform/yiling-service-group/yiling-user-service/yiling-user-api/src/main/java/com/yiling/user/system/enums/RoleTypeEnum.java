package com.yiling.user.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色类型：1-系统角色 2-自定义角色
 * @author dexi.yao
 * @date 2021-06-03
 */
@Getter
@AllArgsConstructor
public enum RoleTypeEnum {
	//系统角色
	SYSTEM(1, "系统角色"),
	//自定义角色
	CUSTOM(2, "自定义角色");

	private Integer code;
	private String  name;

	public static RoleTypeEnum getByCode(Integer code) {
		for (RoleTypeEnum e : RoleTypeEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}

}

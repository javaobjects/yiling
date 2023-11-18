package com.yiling.user.system.enums;

import java.util.List;

import cn.hutool.core.collection.ListUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 菜单类型枚举
 *
 * @author: dexi.yao
 * @date: 2021/5/31
 */
@Getter
@AllArgsConstructor
public enum MenuTypeEnum {
	//目录
	CATALOGUE(1, "目录"),
	//菜单
	MENU(2, "菜单"),
	//按钮
	BUTTON(3, "按钮"),
    ;

	private Integer code;
	private String  name;

	public static MenuTypeEnum getByCode(Integer code) {
		for (MenuTypeEnum e : MenuTypeEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}

    public static List<MenuTypeEnum> all() {
        return ListUtil.empty();
    }

    public static List<MenuTypeEnum> catalogueAndMenu() {
        return ListUtil.toList(CATALOGUE, MENU);
    }
}

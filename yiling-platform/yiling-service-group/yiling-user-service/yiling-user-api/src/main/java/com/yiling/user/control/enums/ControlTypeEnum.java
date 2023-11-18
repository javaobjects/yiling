package com.yiling.user.control.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 控销类型
 * @author shuang.zhang
 * @date 2021-11-14
 */
@Getter
@AllArgsConstructor
public enum ControlTypeEnum {
	/**
	 * 客户控销
	 */
	CUSTOMER(1, "客户控销"),
	/**
	 * 客户类型控销
	 */
	CUSTOMER_TYPE(2, "客户类型控销"),
    /**
     * 区域控销
     */
    REGION(3, "区域控销"),;

	private Integer code;
	private String  name;

	public static ControlTypeEnum getByCode(Integer code) {
		for (ControlTypeEnum e : ControlTypeEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

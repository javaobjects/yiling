package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dexi.yao
 * @date 2021-06-15
 */
@Getter
@AllArgsConstructor
public enum GoodsInventoryLevelEnum {

	//库存为0
	ZERO(0, "库存为0"),
	//小于等于3000
	LESS_THERE_THOUSAND(3000, "库存<=3000"),
	//大于3000
	GREATER_THERE_THOUSAND(3001, "库存>3000");

	private Integer code;
	private String  name;

	public static GoodsInventoryLevelEnum getByCode(Integer code) {
		for (GoodsInventoryLevelEnum e : GoodsInventoryLevelEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

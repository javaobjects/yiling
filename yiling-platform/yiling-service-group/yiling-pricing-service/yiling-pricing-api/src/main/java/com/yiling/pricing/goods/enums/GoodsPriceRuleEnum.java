package com.yiling.pricing.goods.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商品定价规则
 * @author yuecheng.chen
 * @date 2021-06-24
 */
@Getter
@AllArgsConstructor
public enum GoodsPriceRuleEnum {

	/**
	 * 浮点定价
	 */
	FLOAT_PRICE(1, "浮点定价"),
	/**
	 * 具体价格
	 */
	SPECIFIC_PRICE(2, "具体价格");

	private Integer code;
	private String  name;

	public static GoodsPriceRuleEnum getByCode(Integer code) {
		for (GoodsPriceRuleEnum e : GoodsPriceRuleEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

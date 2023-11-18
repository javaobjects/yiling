package com.yiling.goods.medicine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商品状态
 * @author dexi.yao
 * @date 2021-05-24
 */
@Getter
@AllArgsConstructor
public enum GoodsLogEnum {
	/**
	 * 上下架
	 */
	EDIT_STATUS(1, "上下架"),
	/**
	 * 修改库存
	 */
	EDIT_QTY(2, "修改库存"),
	/**
	 * 修改价格
	 */
	EDIT_PRICE(3, "修改价格");

	private Integer code;
	private String  name;

	public static GoodsLogEnum getByCode(Integer code) {
		for (GoodsLogEnum e : GoodsLogEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

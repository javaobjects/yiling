package com.yiling.goods.medicine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 修改商品状态的调用方，用于校验是否有权限修改
 * @author dexi.yao
 * @date 2021-05-24
 */
@Getter
@AllArgsConstructor
public enum GoodsStatusCallerEnum {
	/**
	 * 运营后台
	 */
	ADMIN(1, "运营后台"),
	/**
	 * 供应商后台
	 */
	SUPPLIER_ADMIN(2, "供应商后台");

	private Integer code;
	private String  name;

	public static GoodsStatusCallerEnum getByCode(Integer code) {
		for (GoodsStatusCallerEnum e : GoodsStatusCallerEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

package com.yiling.goods.medicine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 产品线
 * @author shuang.zhang
 * @date 2021-11-14
 */
@Getter
@AllArgsConstructor
public enum GoodsLineEnum {
	/**
	 * pop
	 */
	POP(1, "POP"),
	/**
	 * b2b
	 */
	B2B(2, "B2B"),

    /**
     * HMC
     */
    HMC(3, "HMC"),;

	private Integer code;
	private String  name;

	public static GoodsLineEnum getByCode(Integer code) {
		for (GoodsLineEnum e : GoodsLineEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

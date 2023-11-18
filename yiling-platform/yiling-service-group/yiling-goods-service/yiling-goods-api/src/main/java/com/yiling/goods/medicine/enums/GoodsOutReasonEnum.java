package com.yiling.goods.medicine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商品下架原因
 * @author dexi.yao
 * @date 2021-05-24
 */
@Getter
@AllArgsConstructor
public enum GoodsOutReasonEnum {

    /**
     * 默认
     */
    DEFAULT(0, "默认"),

	/**
	 * 平台下架
	 */
	PLATFORM(1, "平台下架"),
	/**
	 * 质管下架
	 */
	QUALITY_CONTROL(2, "质管下架"),
	/**
	 * 供应商下架
	 */
	REJECT(3, "供应商下架");

	private Integer code;
	private String  name;

	public static GoodsOutReasonEnum getByCode(Integer code) {
        for (GoodsOutReasonEnum e : GoodsOutReasonEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

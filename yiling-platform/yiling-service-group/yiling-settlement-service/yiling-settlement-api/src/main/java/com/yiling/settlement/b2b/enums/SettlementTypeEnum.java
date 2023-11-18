package com.yiling.settlement.b2b.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 结算单类型枚举
 *
 * @author: dexi.yao
 * @date: 2021/10/21
 */
@Getter
@AllArgsConstructor
public enum SettlementTypeEnum {
	//货款结算单
	GOODS(1, "货款结算单"),
	//促销结算单
	SALE(2, "促销结算单"),
	//预售违约金结算单
    PRESALE_DEFAULT(3, "预售违约金结算单");

	private Integer code;
	private String  name;

	public static SettlementTypeEnum getByCode(Integer code) {
		for (SettlementTypeEnum e : SettlementTypeEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

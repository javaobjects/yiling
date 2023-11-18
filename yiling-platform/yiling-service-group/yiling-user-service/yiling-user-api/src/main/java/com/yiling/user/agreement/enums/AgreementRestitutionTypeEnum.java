package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返利形式 1- 票折 2- 现金 3-冲红 4-健康城卡
 * @author dexi.yao
 * @date 2021-07-27
 */
@Getter
@AllArgsConstructor
public enum AgreementRestitutionTypeEnum {

	//未推送
	TICKET(1, "票折"),
	//现金
	CASH(2, "现金"),
	//冲红
	RED_FLUSH(3, "冲红"),
	//健康城卡
	HEALTH_CITY(2, "健康城卡");

	private Integer code;

	private String name;

	public static AgreementRestitutionTypeEnum getByCode(Integer code) {
		for (AgreementRestitutionTypeEnum e : AgreementRestitutionTypeEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

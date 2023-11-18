package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返利申请月度类型枚举 月度类型 1-月度 2-季度 3-上半年 4-下半年 5-全年
 * @author dexi.yao
 * @date 2021-07-27
 */
@Getter
@AllArgsConstructor
public enum AgreementRebateApplyRangeTypeEnum {

	//月度
	MONTH(1, "月"),
	//季度
	QUARTER(2, "季度"),
	//上半年
	FIRST_HALF_YEAR(3, "上半年"),
	//下半年
	SECOND_HALF_YEAR(4, "下半年"),
	//全年
	ALL_YEAR(5, "全年");

	private Integer code;

	private String name;

	public static AgreementRebateApplyRangeTypeEnum getByCode(Integer code) {
		for (AgreementRebateApplyRangeTypeEnum e : AgreementRebateApplyRangeTypeEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

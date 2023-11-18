package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返利条件规则枚举
 * 1-购进总额 2-月度拆解 3-季度拆解 4-梯度
 * @author dexi.yao
 * @date 2021-06-24
 */
@Getter
@AllArgsConstructor
public enum AgreementConditionRuleEnum {
    //订单金额
    ORDER_AMOUNT(0, "采购额"),
	//购进总额
	TOTAL_AMOUNT(1, "总额"),
	//月度拆解
	MONTHLY(2, "月度"),
	//季度拆解
	SEASONS(3, "季度"),
	//梯度
	GRADIENT(4, "梯度"),
    //固定天数 比如25以前
    CONFIRM_DATA(5, "回款节点");

	private Integer code;

	private String name;

	public static AgreementConditionRuleEnum getByCode(Integer code) {
		for (AgreementConditionRuleEnum e : AgreementConditionRuleEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

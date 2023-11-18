package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dexi.yao
 * @date 2021-06-23
 */
@Getter
@AllArgsConstructor
public enum AgreementRebateCycleEnum {

	//订单立返
    IMMEDIATELY_REBATE(1, "订单立返"),
	//进入返利池
    REBATE_POOL(2, "进入返利池");

	private Integer code;

	private String name;

	public static AgreementRebateCycleEnum getByCode(Integer code) {
		for (AgreementRebateCycleEnum e : AgreementRebateCycleEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

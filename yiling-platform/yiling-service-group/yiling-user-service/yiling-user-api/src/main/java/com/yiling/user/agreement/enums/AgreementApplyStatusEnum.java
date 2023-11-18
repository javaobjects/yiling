package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返利申请状态枚举 状态 1-待审核 2-已入账 3-驳回
 * @author dexi.yao
 * @date 2021-07-27
 */
@Getter
@AllArgsConstructor
public enum AgreementApplyStatusEnum {

	//待审核
	CHECK(1, "待审核"),
	//已入账/审核成功
	SUCCESS(2, "已入账"),
	//驳回
	REJECT(3, "驳回");

	private Integer code;

	private String name;

	public static AgreementApplyStatusEnum getByCode(Integer code) {
		for (AgreementApplyStatusEnum e : AgreementApplyStatusEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

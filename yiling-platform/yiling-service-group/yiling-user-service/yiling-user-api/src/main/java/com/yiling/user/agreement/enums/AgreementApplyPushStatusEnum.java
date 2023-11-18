package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否推送至冲红系统 1-未推送 2-已推送
 * @author dexi.yao
 * @date 2021-07-27
 */
@Getter
@AllArgsConstructor
public enum AgreementApplyPushStatusEnum {

	//未推送
	NOT_PUSHED(1, "未推送"),
	//已推送
	PUSHED(2, "已推送");

	private Integer code;

	private String name;

	public static AgreementApplyPushStatusEnum getByCode(Integer code) {
		for (AgreementApplyPushStatusEnum e : AgreementApplyPushStatusEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

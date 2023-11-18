package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 申请单状态1-草稿 2-提交 3-审核成功 4-驳回 -5撤回
 * @author dexi.yao
 * @date 2021-07-27
 */
@Getter
@AllArgsConstructor
public enum AgreementUseStatusEnum {

	//草稿
	DRAFT(1, "草稿"),
	//提交
	SUBMIT(2, "提交"),
	//审核成功
	SUCCESS(3, "审核成功"),
	//驳回
	REJECTED(4, "驳回"),
	//撤回
	WITHDRAW(5, "撤回");

	private Integer code;

	private String name;

	public static AgreementUseStatusEnum getByCode(Integer code) {
		for (AgreementUseStatusEnum e : AgreementUseStatusEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

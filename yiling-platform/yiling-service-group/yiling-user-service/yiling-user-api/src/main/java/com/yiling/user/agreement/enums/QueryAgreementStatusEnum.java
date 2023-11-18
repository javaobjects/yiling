package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 查询协议状态：1-进行中 2-未开始 3-已停用 4-已过期
 * @author dexi.yao
 * @date 2021-06-04
 */
@Getter
@AllArgsConstructor
public enum QueryAgreementStatusEnum {

	//进行中
	HAVE_IN_HAND(1, "进行中"),
	//未开始
	NOT_STARTED(2, "未开始"),
	//已停用
	OUT_OF_SERVICE(3, "已停用"),
	//已过期
	EXPIRED(4, "已过期"),
	//进行中&未开始
	HAVE_IN_HAND_NOT_STARTED(5, "进行中&未开始");

	private Integer code;

	private String name;

	public static QueryAgreementStatusEnum getByCode(Integer code) {
		for (QueryAgreementStatusEnum e : QueryAgreementStatusEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

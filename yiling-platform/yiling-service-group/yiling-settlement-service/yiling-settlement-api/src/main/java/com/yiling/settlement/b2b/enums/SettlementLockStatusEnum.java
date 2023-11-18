package com.yiling.settlement.b2b.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 结算单锁定类型枚举
 *
 * @author: dexi.yao
 * @date: 2021/10/21
 */
@Getter
@AllArgsConstructor
public enum SettlementLockStatusEnum {
	//未锁定
	UN_LOCK(1, "未锁定"),
	//已锁定
	LOCKED(2, "已锁定");

	private Integer code;
	private String  name;

	public static SettlementLockStatusEnum getByCode(Integer code) {
		for (SettlementLockStatusEnum e : SettlementLockStatusEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}

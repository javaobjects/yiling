package com.yiling.settlement.b2b.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 结算单状态(1-未结算,2-已结算)
 *
 * @author: dexi.yao
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum B2BOrderSettStatusEnum {

	//未结算
	UN_SETTLEMENT(1, "未结算"),
	//已结算
	SETTLEMENT(2, "已结算"),
    ;

    private Integer code;
    private String name;

    public static B2BOrderSettStatusEnum getByCode(Integer code) {
        for (B2BOrderSettStatusEnum e : B2BOrderSettStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

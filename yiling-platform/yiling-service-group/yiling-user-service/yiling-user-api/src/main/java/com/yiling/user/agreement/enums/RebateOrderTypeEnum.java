package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单类型枚举
 *
 * @author: dexi.yao
 * @date: 2021/8/23
 */
@Getter
@AllArgsConstructor
public enum RebateOrderTypeEnum {

	//订单
    ORDER(1, "订单"),
	//退款单
	REFUND(2, "退款单"),
	//结算单
	SETTLEMENT(3, "结算单"),
    ;

    private Integer code;
    private String name;

    public static RebateOrderTypeEnum getByCode(Integer code) {
        for (RebateOrderTypeEnum e : RebateOrderTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

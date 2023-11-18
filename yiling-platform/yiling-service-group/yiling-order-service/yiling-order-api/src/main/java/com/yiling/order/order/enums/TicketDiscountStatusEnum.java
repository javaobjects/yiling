package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 票折信息状态枚举 1-启用 2-停用
 *
 * @author: dexi.yao
 * @date: 2021/8/4
 */
@Getter
@AllArgsConstructor
public enum TicketDiscountStatusEnum {

	//1-启用
    OPEN(1, "启用"),
	//2-停用
    CLOSE(2, "停用");

    private Integer code;
    private String name;

    public static TicketDiscountStatusEnum getByCode(Integer code) {
        for (TicketDiscountStatusEnum e : TicketDiscountStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

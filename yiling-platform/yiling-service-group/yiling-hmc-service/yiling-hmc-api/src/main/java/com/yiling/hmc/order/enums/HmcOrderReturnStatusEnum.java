package com.yiling.hmc.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 退货单状态：1-待审核/2-已退/3-已取消退单
 *
 * @author: yong.zhang
 * @date: 2022/3/25
 */
@Getter
@AllArgsConstructor
public enum HmcOrderReturnStatusEnum {

    /**
     * 1-待审核
     */
    UN_CHECK(1, "待审核"),
    /**
     * 2-已退
     */
    RETURNED(2, "已退"),
    /**
     * 3-已取消退单
     */
    CANCEL(3, "已取消退单"),
    ;

    private final Integer code;
    private final String name;

    public static HmcOrderReturnStatusEnum getByCode(Integer code) {
        for (HmcOrderReturnStatusEnum e : HmcOrderReturnStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

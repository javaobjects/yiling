package com.yiling.hmc.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作功能:1-自提/2-发货/3-退货/4-收货
 *
 * @author: yong.zhang
 * @date: 2022/4/21
 */
@Getter
@AllArgsConstructor
public enum HmcOrderOperateTypeEnum {

    /**
     * 1-自提
     */
    SELF_PICKUP(1, "自提"),
    /**
     * 2-发货
     */
    DELIVER(2, "发货"),
    /**
     * 3-退货
     */
    RETURN(3, "退货"),
    /**
     * 4-收货
     */
    RECEIVE(4, "收货"),

    /**
     * 5-创建订单
     */
    CREATE_ORDER(5, "创建订单"),
    ;

    private final Integer code;
    private final String name;
}

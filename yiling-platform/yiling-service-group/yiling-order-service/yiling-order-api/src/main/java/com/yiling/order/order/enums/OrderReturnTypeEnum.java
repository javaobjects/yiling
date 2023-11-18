package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 退款单类型枚举
 *
 * @author: tingwei.chen
 * @date: 2021/6/25
 */
@Getter
@AllArgsConstructor
public enum OrderReturnTypeEnum {

                                 /**
                                  * 驳回退货单
                                  */
                                 SELLER_RETURN_ORDER(1, "驳回退货单"),
                                 /**
                                  * 退回退货单
                                  */
                                 DAMAGE_RETURN_ORDER(2, "退回退货单"),
                                 /**
                                  * 采购退货单
                                  */
                                 BUYER_RETURN_ORDER(3, "采购退货单");

    private final Integer code;
    private final String  name;

    /**
     * 根据编号查询枚举信息
     *
     * @param code  编号
     * @return  枚举信息
     */
    public static OrderReturnTypeEnum getByCode(Integer code) {
        for (OrderReturnTypeEnum e : OrderReturnTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

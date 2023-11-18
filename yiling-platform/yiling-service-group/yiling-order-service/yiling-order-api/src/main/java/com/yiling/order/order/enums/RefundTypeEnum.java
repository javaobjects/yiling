package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 退款类型
 *
 * @author: yong.zhang
 * @date: 2021/11/23
 */
@Getter
@AllArgsConstructor
public enum RefundTypeEnum {
    //退款类型1-订单取消退款 2-采购退货退款 3-商家驳回
    ORDER_CANCEL(1, "订单取消退款"),
    BUYER_RETURN(2, "采购退货退款"),
    SELLER_RETURN(3, "商家驳回退款"),
    MEMBER_RETURN(4, "会员订单退款"),
    ;

    private Integer code;
    private String  name;

    public static RefundTypeEnum getByCode(Integer code) {
        for (RefundTypeEnum e : RefundTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

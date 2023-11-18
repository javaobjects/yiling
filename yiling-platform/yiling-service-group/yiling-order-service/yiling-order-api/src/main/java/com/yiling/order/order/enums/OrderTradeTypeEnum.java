package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 订单交易类型
 * @author zhigang.guo
 * @date: 2022/10/10
 */
@Getter
@AllArgsConstructor
public enum OrderTradeTypeEnum {

    FULL(1, "全款"),
    DEPOSIT(2, "定金"),
    BALANCE(3, "尾款"),
    ;

    private Integer code;
    private String name;

    public static PromotionActivityTypeEnum getByCode(Integer code) {
        for (PromotionActivityTypeEnum e : PromotionActivityTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

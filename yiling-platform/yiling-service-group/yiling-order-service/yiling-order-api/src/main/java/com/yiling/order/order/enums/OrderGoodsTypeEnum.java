package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单商品类型
 *
 * @author zhigang.guo
 * @date: 2022/5/9
 */
@Getter
@AllArgsConstructor
public enum OrderGoodsTypeEnum {
    NORMAL(0, "普通商品"), YLGOODS(1, "以岭商品");

    private Integer code;
    private String name;


    public static OrderGoodsTypeEnum getByCode(Integer code) {
        for (OrderGoodsTypeEnum e : OrderGoodsTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

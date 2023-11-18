package com.yiling.mall.cart.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 购物车商品来源
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.mall.cart.enums
 * @date: 2021/12/17
 */
@Getter
@AllArgsConstructor
public enum CartGoodsSourceEnum {
    // POP商品
    POP(1, "POP商品"),
    // B2B商品
    B2B(2, "B2B商品");

    private Integer code;
    private String  name;

    public static CartGoodsSourceEnum getByCode(Integer code) {
        for (CartGoodsSourceEnum e : CartGoodsSourceEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}

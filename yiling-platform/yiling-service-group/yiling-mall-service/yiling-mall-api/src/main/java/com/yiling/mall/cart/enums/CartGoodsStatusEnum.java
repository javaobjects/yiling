package com.yiling.mall.cart.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 购物车商品状态枚举
 *
 * @author: xuan.zhou
 * @date: 2021/6/21
 */
@Getter
@AllArgsConstructor
public enum CartGoodsStatusEnum {

    NORMAL(1, "正常"),
    UN_SHELF(2, "下架"),
    NO_STOCK(3, "无货"),
    INVALID(4,"失效"),
    ;

    private Integer code;
    private String name;
}

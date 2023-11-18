package com.yiling.mall.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2021/6/17
 */
@AllArgsConstructor
@Getter
public enum GoodsLimitStatusEnum {
    NORMAL(1, "加入进货单"),
    OUT_OF_RELATION(2, "无采购关系"),
    NOT_BUY(3, "不在采购协议内"),
    NOT_LOGIN(4, "未登录"),
    CONTROL_GOODS(5, "控销品种"),
    SHOP_CONTROL(6, "控销区域"),
    UN_SHELF(7, "下架商品"),
    NOT_RELATION_SHIP(8, "去建采"),
    AUDIT_RELATION_SHIP(9, "审核中"),
    INVALID_GOODS(10,"失效商品");

    private Integer code;
    private String  name;

    public static String getNameByCode(Integer code) {
        for (GoodsLimitStatusEnum em : values()) {
            if (em.getCode().equals(code)) {
                return em.getName();
            }
        }
        return null;
    }


    public static GoodsLimitStatusEnum getByCode(Integer code) {
        for (GoodsLimitStatusEnum e : GoodsLimitStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

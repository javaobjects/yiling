package com.yiling.marketing.goodsgift.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 赠品 商品类别
 *
 * @author: houjie.sun
 * @date: 2021/10/23
 */
@Getter
@AllArgsConstructor
public enum GoodsGiftTypeEnum {

    /**
     * 1-真实物品
     */
    REAL_GOODS(1, "真实物品"),
    /**
     * 2-虚拟物品
     */
    INVENT_GOODS(2, "虚拟物品"),
    /**
     * 3-优惠券
     */
    COUPON(3, "优惠券"),
    /**
     * 4-会员
     */
    MEMBER(4, "会员"),
    ;

    private Integer code;
    private String  name;

    public static GoodsGiftTypeEnum getByCode(Integer code) {
        for (GoodsGiftTypeEnum e : GoodsGiftTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

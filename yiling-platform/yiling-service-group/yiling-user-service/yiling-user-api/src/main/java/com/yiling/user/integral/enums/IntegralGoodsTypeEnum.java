package com.yiling.user.integral.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 积分兑换商品类型枚举
 *
 * @author: lun.yu
 * @date: 2023-01-09
 */
@Getter
@AllArgsConstructor
public enum IntegralGoodsTypeEnum {

    /**
     * 商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券
     */
    REAL_GOODS(1, "真实物品"),
    VIRTUAL_GOODS(2, "虚拟物品"),
    GOODS_COUPON(3, "商品优惠券"),
    MEMBER_COUPON(4, "会员优惠券"),
    ;

    private final Integer code;
    private final String name;

    public static IntegralGoodsTypeEnum getByCode(Integer code) {
        for (IntegralGoodsTypeEnum e : IntegralGoodsTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

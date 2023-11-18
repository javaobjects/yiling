package com.yiling.user.integral.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单送积分-商品范围枚举
 *
 * @author: lun.yu
 * @date: 2022-12-30
 */
@Getter
@AllArgsConstructor
public enum IntegralGoodsScopeEnum {

    // 商品范围：1-全部商品 2-指定平台SKU 3-指定店铺SKU
    ALL_GOODS(1, "全部商品"),
    PLATFORM_SKU(2, "指定平台SKU"),
    SHOP_SKU(3, "指定店铺SKU"),
    ;

    private final Integer code;
    private final String name;

    public static IntegralGoodsScopeEnum getByCode(Integer code) {
        for (IntegralGoodsScopeEnum e : IntegralGoodsScopeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

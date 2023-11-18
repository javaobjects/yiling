package com.yiling.marketing.strategy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 商品范围类型（1-全部商品；2-指定平台SKU；3-指定店铺SKU；）
 *
 * @author: yong.zhang
 * @date: 2022/8/24
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum StrategyConditionGoodsTypeEnum {
    /**
     * 全部商品
     */
    ALL(1, "全部商品"),
    /**
     * 指定平台SKU
     */
    PLATFORM(2, "指定平台SKU"),
    /**
     * 指定店铺SKU
     */
    ENTERPRISE(3, "指定店铺SKU"),
    ;

    private Integer type;
    private String name;

    public static StrategyConditionGoodsTypeEnum getByType(Integer type) {
        for (StrategyConditionGoodsTypeEnum e : StrategyConditionGoodsTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }
}

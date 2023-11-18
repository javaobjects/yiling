package com.yiling.marketing.strategy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 商家范围类型（1-全部商家；2-指定商家；）
 *
 * @author: yong.zhang
 * @date: 2022/8/24
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum StrategyConditionSellerTypeEnum {
    /**
     * 全部商家
     */
    ALL(1, "全部商家"),
    /**
     * 指定商家
     */
    ASSIGN(2, "指定商家"),
    ;

    private Integer type;
    private String name;

    public static StrategyConditionSellerTypeEnum getByType(Integer type) {
        for (StrategyConditionSellerTypeEnum e : StrategyConditionSellerTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }
}

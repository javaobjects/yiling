package com.yiling.marketing.strategy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 商户范围类型（1-全部客户；2-指定客户；3-指定范围客户）
 *
 * @author: yong.zhang
 * @date: 2022/8/24
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum StrategyConditionBuyerTypeEnum {
    
    /**
     * 全部客户
     */
    ALL(1, "全部客户"),
    /**
     * 指定客户
     */
    ASSIGN(2, "指定客户"),
    /**
     * 指定范围客户
     */
    RANGE(3, "指定范围客户"),
    ;

    private Integer type;
    private String name;

    public static StrategyConditionBuyerTypeEnum getByType(Integer type) {
        for (StrategyConditionBuyerTypeEnum e : StrategyConditionBuyerTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }
}

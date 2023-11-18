package com.yiling.marketing.strategy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 指定企业类型(1:全部类型 2:指定类型)
 *
 * @author: yong.zhang
 * @date: 2022/8/24
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum StrategyConditionEnterpriseTypeEnum {

    /**
     * 全部类型
     */
    ALL(1, "全部类型"),
    /**
     * 指定类型
     */
    ASSIGN(2, "指定类型"),
    ;

    private Integer type;
    private String name;

    public static StrategyConditionEnterpriseTypeEnum getByType(Integer type) {
        for (StrategyConditionEnterpriseTypeEnum e : StrategyConditionEnterpriseTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }
}

package com.yiling.marketing.strategy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 指定部分会员用户类型（1-指定方案会员；2-指定推广方会员；）
 *
 * @author: yong.zhang
 * @date: 2022/11/23 0023
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum StrategyConditionUserMemberTypeEnum {

    /**
     * 指定方案会员
     */
    PROGRAM_MEMBER(1, "指定方案会员"),
    /**
     * 指定推广方会员
     */
    PROMOTER_MEMBER(2, "指定推广方会员"),
    ;

    private Integer type;
    private String name;

    public static StrategyConditionUserMemberTypeEnum getByType(Integer type) {
        for (StrategyConditionUserMemberTypeEnum e : StrategyConditionUserMemberTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }
}

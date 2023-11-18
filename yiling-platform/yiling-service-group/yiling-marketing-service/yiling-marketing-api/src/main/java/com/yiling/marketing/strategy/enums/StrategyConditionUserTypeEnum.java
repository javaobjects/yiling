package com.yiling.marketing.strategy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分会员）
 *
 * @author: yong.zhang
 * @date: 2022/8/24
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum StrategyConditionUserTypeEnum {

    /**
     * 全部用户
     */
    ALL(1, "全部用户"),
    /**
     * 仅普通用户
     */
    COMMON(2, "仅普通用户"),
    /**
     * 全部会员用户
     */
    ALL_MEMBER(3, "全部会员用户"),
    /**
     * 部分会员
     */
    PART_MEMBER(4, "部分会员"),
    ;

    private Integer type;
    private String name;

    public static StrategyConditionUserTypeEnum getByType(Integer type) {
        for (StrategyConditionUserTypeEnum e : StrategyConditionUserTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }
}

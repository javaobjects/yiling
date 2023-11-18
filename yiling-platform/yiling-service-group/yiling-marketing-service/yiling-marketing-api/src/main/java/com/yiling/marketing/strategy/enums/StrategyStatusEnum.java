package com.yiling.marketing.strategy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 状态：1-启用 2-停用 3-废弃
 *
 * @author: yong.zhang
 * @date: 2022/8/30
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum StrategyStatusEnum {

    /**
     * 1-启用
     */
    ENABLE(1, "启用"),
    /**
     * 2-停用
     */
    UNABLE(2, "停用"),
    /**
     * 3-废弃
     */
    DISCARD(3, "废弃"),
    ;

    private Integer code;
    private String name;

    public static StrategyStatusEnum getByType(Integer code) {
        for (StrategyStatusEnum e : StrategyStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

package com.yiling.marketing.promotion.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 促销活动-终端身份枚举类
 * @Author fan.shen
 * @Date 2021/12/20
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum PromotionTerminalTypeEnum {

    MEMBER(1, "会员"),

    NOT_MEMBER(2, "非会员"),

    ALL(0, "不限制"),

    ;

    private Integer type;

    private String  name;

    public static PromotionTerminalTypeEnum getByType(Integer type) {
        for (PromotionTerminalTypeEnum e : PromotionTerminalTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

}

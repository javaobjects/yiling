package com.yiling.marketing.promotion.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 促销活动-状态枚举类
 * @Author fan.shen
 * @Date 2022/01/13
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum PromotionStatusEnum {

    /**
     * 启用
     */
    ENABLED(1, "启用"),

    /**
     * 停用
     */
    STOPPED(2, "停用");

    private Integer type;

    private String  name;

    public static PromotionStatusEnum getByType(Integer type) {
        for (PromotionStatusEnum e : PromotionStatusEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

}

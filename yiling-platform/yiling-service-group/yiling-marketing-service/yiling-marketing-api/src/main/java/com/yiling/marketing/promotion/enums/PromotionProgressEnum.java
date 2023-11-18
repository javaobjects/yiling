package com.yiling.marketing.promotion.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 促销活动-进度枚举类
 * @Author fan.shen
 * @Date 2022/03/4
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum PromotionProgressEnum {

    /**
     * 待开始
     */
    READY(1, "待开始"),

    /**
     * 进行中
     */
    PROCESSING(2, "进行中"),

    /**
     * 已结束
     */
    END(3, "已结束");

    private Integer type;

    private String  name;

    public static PromotionProgressEnum getByType(Integer type) {
        for (PromotionProgressEnum e : PromotionProgressEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

}

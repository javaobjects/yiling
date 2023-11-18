package com.yiling.marketing.promotion.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 促销活动-费用承担方枚举类
 * @Author fan.shen
 * @Date 2021/12/20
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum PromotionBearTypeEnum {

    PLATFORM(1, "平台"),

    MERCHANT(2, "商家"),

    SHARE(3, "分摊");

    private Integer type;

    private String  name;

    public static PromotionBearTypeEnum getByType(Integer type) {
        for (PromotionBearTypeEnum e : PromotionBearTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

}

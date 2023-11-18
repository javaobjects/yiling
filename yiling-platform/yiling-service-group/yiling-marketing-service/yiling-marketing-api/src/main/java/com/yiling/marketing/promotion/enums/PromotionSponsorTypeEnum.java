package com.yiling.marketing.promotion.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 促销活动-活动分类枚举类
 * @Author fan.shen
 * @Date 2021/12/20
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum PromotionSponsorTypeEnum {

    PLATFORM(1, "平台活动"),

    MERCHANT(2, "商家活动")

    ;

    private Integer type;

    private String  name;

    public static PromotionSponsorTypeEnum getByType(Integer type) {
        for (PromotionSponsorTypeEnum e : PromotionSponsorTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

}

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
public enum PromotionMerchantTypeEnum {

    YL(1, "以岭"),

    NOT_YL(2, "非以岭"),

    ALL(0, "不限制"),
    ;

    private Integer type;

    private String  name;

    public static PromotionMerchantTypeEnum getByType(Integer type) {
        for (PromotionMerchantTypeEnum e : PromotionMerchantTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

}

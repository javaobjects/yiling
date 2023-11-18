package com.yiling.marketing.promotion.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 促销活动-允许购买类型枚举类
 * @Author fan.shen
 * @Date 2021/12/20
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum PromotionPermittedTypeEnum {

    ALL(1, "全部"),

    PART(2, "部分"),

    ;

    private Integer type;

    private String  name;

    public static PromotionPermittedTypeEnum getByType(Integer type) {
        for (PromotionPermittedTypeEnum e : PromotionPermittedTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

}

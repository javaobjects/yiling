package com.yiling.order.order.enums;

import java.util.EnumSet;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.enums
 * @date: 2022/2/7
 */
@Getter
@AllArgsConstructor
public enum  PromotionActivityTypeEnum {

    NORMAL(0, "正常"),
    FULL_GIFT(1, "满赠活动"),
    SPECIAL(2, "特价活动"),
    LIMIT(3, "秒杀活动"),
    COMBINATION(4, "组合促销活动"),
    PAYMENT(5, "支付促销活动"),
    PRESALE(6, "预售促销活动"),
    ;

    private Integer code;
    private String name;

    public static PromotionActivityTypeEnum getByCode(Integer code) {
        for (PromotionActivityTypeEnum e : PromotionActivityTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}

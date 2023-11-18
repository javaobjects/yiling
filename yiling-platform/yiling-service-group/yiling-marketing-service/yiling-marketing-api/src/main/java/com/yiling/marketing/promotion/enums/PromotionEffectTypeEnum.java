package com.yiling.marketing.promotion.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 促销活动-秒杀&特价生效类型
 * @author: fan.shen
 * @date: 2021/12/22
 */
@Getter
@AllArgsConstructor
public enum PromotionEffectTypeEnum{

    /**
     * 立即生效
     */
    NOW(1, "立即生效"),

    /**
     * 固定时间生效
     */
    FIXED(2, "固定时间生效"),
    ;

    private final Integer code;
    private final String  message;
}

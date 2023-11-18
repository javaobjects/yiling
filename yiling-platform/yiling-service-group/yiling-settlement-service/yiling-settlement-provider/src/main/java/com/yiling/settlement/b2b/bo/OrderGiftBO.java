package com.yiling.settlement.b2b.bo;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 赠品订单BO
 * @author dexi.yao
 * @date 2022-02-17
 */
@Data
@Accessors(chain = true)
public class OrderGiftBO {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 价格区间ID
     */
    private Long promotionLimitId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 活动类型（1-满赠 2-特价 3-秒杀）
     */
    private Integer activityType;

    /**
     * 活动费用承担方（1-平台 2-商家 3-分摊）
     */
    private Integer activityBear;

    /**
     * 活动费用平台承担百分比
     */
    private BigDecimal activityPlatformPercent;

    /**
     * 赠品ID
     */
    private Long goodsGiftId;

    /**
     * 赠品价格
     */
    private BigDecimal price;
}

package com.yiling.order.order.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.dto
 * @date: 2022/2/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderPromotionActivityDTO extends BaseDTO {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 活动类型（1-满赠 2-特价 3-秒杀 4-组合促销）
     * {@link com.yiling.order.order.enums.PromotionActivityTypeEnum }
     */
    private Integer activityType;

    /**
     * 活动分类（1-平台活动；2-商家活动；）
     */
    private Integer sponsorType;

    /**
     * 活动费用承担方（1-平台 2-商家 3-分摊）
     */
    private Integer activityBear;

    /**
     * 活动费用平台承担百分比
     */
    private BigDecimal activityPlatformPercent;

    /**
     * 组合优惠活动套数,其他促销活动,默认为1
     */
    private Integer activityNum;

}

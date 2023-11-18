package com.yiling.order.order.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.dto.request
 * @date: 2021/10/27
 */
@Data
@Accessors(chain = true)
@ToString
public class CreateOrderPromotionActivityRequest extends BaseRequest {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 活动规则Id
     */
    private Long activityRuleId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 活动类型（1-满赠 2-特价 3-秒杀 4-组合优惠 5-支付促销 6-预售活动）
     * {@Link com.yiling.order.order.enums.PromotionActivityTypeEnum }
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
     * 活动套数,组合优惠记录，购买套数，其他促销活动默认为1
     */
    private Integer activityNum;


}

package com.yiling.marketing.paypromotion.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 营销活动主表
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PayPromotionParticipateDTO extends BaseDTO {

    /**
     * 支付促销活动id
     */
    private Long marketingPayId;

    /**
     * 买家企业ID
     */
    private Long eid;

    /**
     * 买家企业名称
     */
    private String ename;

    /**
     * 参与时间
     */
    private Date participateTime;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 计算区间id
     */
    private Long ruleId;

    /**
     * 订单实付金额
     */
    private BigDecimal payment;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 装填
     */
    private String status;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 活动名称
     */
    private String name;
}

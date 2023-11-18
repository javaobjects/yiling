package com.yiling.marketing.couponactivity.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/12/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityUsedPageDTO extends BaseDTO {

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 优惠券活动id
     */
    private Long couponActivityId;

    /**
     * 优惠劵活动名称
     */
    private String couponName;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 优惠金额
     */
    private BigDecimal amount;

    /**
     * 优惠规则
     */
    private String couponRules;

    /**
     * 获券企业id
     */
    private Long eid;

    /**
     * 获券企业名称
     */
    private String ename;

    /**
     * 发放方式（1-运营发放；2-自动发放；3-自主领取；4-促销活动赠送）
     */
    private Integer getType;

    /**
     * 发放方式（1-运营发放；2-自动发放；3-自主领取；4-促销活动赠送）
     */
    private String getTypeStr;

    /**
     * 有效期
     */
    private String effectiveTime;

    /**
     * 发放/领取时间
     */
    private Date getTime;

    /**
     * 用券时间
     */
    private Date useTime;

}

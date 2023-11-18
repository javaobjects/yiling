package com.yiling.b2b.admin.coupon.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/3
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityUsedPageVo extends BaseVO {

    /**
     * 优惠券id
     */
    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

    /**
     * 优惠券活动id
     */
    @ApiModelProperty(value = "优惠券活动id")
    private Long couponActivityId;

    /**
     * 优惠劵活动名称
     */
    @ApiModelProperty(value = "优惠劵活动名称")
    private String couponName;

    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 订单金额
     */
    @ApiModelProperty(value = "订单金额")
    private BigDecimal orderAmount;

    /**
     * 优惠金额
     */
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal amount;

    /**
     * 优惠规则
     */
    @ApiModelProperty(value = "优惠规则")
    private String couponRules;

    /**
     * 获券企业id
     */
    @ApiModelProperty(value = "获券企业id")
    private Long eid;

    /**
     * 获券企业名称
     */
    @ApiModelProperty(value = "获券企业名称")
    private String ename;

    /**
     * 发放方式（1-运营发放；2-自动发放；3-自主领取；4-促销活动赠送）
     */
    @ApiModelProperty(value = "发放方式（1-运营发放；2-自动发放；3-自主领取；4-促销活动赠送）")
    private Integer getType;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期")
    private String effectiveTime;

    /**
     * 发放/领取时间
     */
    @ApiModelProperty(value = "发放/领取时间")
    private Date getTime;

    /**
     * 用券时间
     */
    @ApiModelProperty(value = "用券时间")
    private Date useTime;

}

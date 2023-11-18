package com.yiling.b2b.app.coupon.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/17
 */
@Data
@Accessors(chain = true)
public class MyMemberCouponVO extends BaseVO {

    /**
     * 优惠券id
     */
    @ApiModelProperty(required = true, value = "优惠券活动ID")
    private Long activityId;
    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 优惠内容（金额）
     */
    @ApiModelProperty(required = true, value = "优惠券活动ID")
    private BigDecimal discountValue;

    /**
     * 用券开始时间
     */
    @ApiModelProperty(required = true, value = "优惠券活动ID")
    private Date beginTime;

    /**
     * 用券结束时间
     */
    @ApiModelProperty(required = true, value = "优惠券活动ID")
    private Date endTime;

    /**
     * 类型（1-满减券；2-满折券）
     */
    @ApiModelProperty(required = true, value = "类型（1-满减券；2-满折券）")
    private Integer type;
}

package com.yiling.admin.b2b.coupon.from;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCouponActivityAutoGiveCouponFrom extends BaseForm {

    /**
     * 优惠券活动ID
     */
    @ApiModelProperty("优惠券活动ID")
    @NotNull
    private Long couponActivityId;

    /**
     * 自动发券数量
     */
    @ApiModelProperty("自动发券数量")
    @NotNull
    private Integer giveNum;

    /**
     * 优惠券活动开始时间
     */
    @ApiModelProperty("优惠券活动开始时间")
    @NotNull
    private Date beginTime;

    /**
     * 优惠券活动结束时间
     */
    @ApiModelProperty("优惠券活动结束时间")
    @NotNull
    private Date endTime;

    /**
     * 生效类型1固定生效时间 2领券后生效
     */
    @ApiModelProperty("生效类型1固定生效时间 2领券后生效")
    @NotNull
    private Integer useDateType;
}

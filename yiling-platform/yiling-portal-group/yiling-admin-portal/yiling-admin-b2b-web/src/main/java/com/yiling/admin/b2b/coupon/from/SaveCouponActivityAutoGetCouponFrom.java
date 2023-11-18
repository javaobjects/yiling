package com.yiling.admin.b2b.coupon.from;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCouponActivityAutoGetCouponFrom extends BaseForm {

    /**
     * 优惠券券活动ID
     */
    @ApiModelProperty("优惠券券活动ID")
    @NotNull
    private Long couponActivityId;

    /**
     * 可领取数量
     */
    @ApiModelProperty("可领取数量")
    @NotNull
    @Max(value = 99)
    private Integer giveNum;

    /**
     * 可领取数量
     */
    @ApiModelProperty("每人每日领取数量")
    private Integer giveNumDaily;

    /**
     * 1固定生效时间 2领券后生效
     */
    @ApiModelProperty("可领取数量")
    private Integer useDateType;
}

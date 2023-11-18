package com.yiling.admin.b2b.coupon.from;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
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
public class SaveCouponActivityAutoGiveBasicFrom extends BaseForm {

    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 自动发券活动名称
     */
    @ApiModelProperty("自动发券活动名称")
    @NotEmpty
    private String name;

    /**
     * 活动开始时间
     */
    @ApiModelProperty("活动开始时间")
    @NotNull
    private Date beginTime;

    /**
     * 活动结束时间
     */
    @ApiModelProperty("活动结束时间")
    @NotNull
    private Date endTime;

    /**
     * 关联优惠券列表
     */
    @ApiModelProperty("关联优惠券列表")
    @NotNull
    private List<SaveCouponActivityAutoGiveCouponFrom> couponActivityList;

}

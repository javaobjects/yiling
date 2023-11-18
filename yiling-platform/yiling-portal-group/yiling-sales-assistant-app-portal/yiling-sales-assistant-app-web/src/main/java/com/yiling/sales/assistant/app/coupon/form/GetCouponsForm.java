package com.yiling.sales.assistant.app.coupon.form;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 领取优惠券Form
 *
 * @author houjie.sun
 * @date 2022/04/06
 */
@Data
@ApiModel
public class GetCouponsForm extends BaseForm {

    /**
     * 选择的代下单客户企业ID
     */
    @NotNull
    @Min(1)
    @ApiModelProperty("选择的代下单客户企业ID")
    private Long customerEid;

    /**
     * 优惠券活动ID
     */
    @NotNull
    @Min(1)
    @ApiModelProperty("优惠券活动ID")
    private Long couponActivityId;

}

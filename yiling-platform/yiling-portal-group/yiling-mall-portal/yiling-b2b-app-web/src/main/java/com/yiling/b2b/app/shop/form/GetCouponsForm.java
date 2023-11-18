package com.yiling.b2b.app.shop.form;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 领取优惠券Form
 * @author lun.yu
 * @date 2021/11/12
 */
@Data
@ApiModel
public class GetCouponsForm extends BaseForm {

    /**
     * 优惠券活动ID
     */
    @NotNull
    @Min(1)
    @ApiModelProperty("优惠券活动ID")
    private Long couponActivityId;

}

package com.yiling.admin.hmc.welfare.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: benben.jia
 * @data: 2022/09/30
 */
@Data
public class DrugWelfareCouponForm extends BaseForm {


    /**
     * 药品福利券包id
     */
    @ApiModelProperty("药品福利券包id")
    private Long id;

    /**
     * 药品福利id
     */
    @ApiModelProperty("药品福利id")
    private Long drugWelfareId;

    /**
     * 要求达到的数量,满几盒
     */
    @ApiModelProperty("要求达到的数量,满几盒")
    @NotNull(message = "要求达到的数量,满几盒_不能为空")
    private Long requirementNumber;

    /**
     * 赠送数量,赠几盒
     */
    @ApiModelProperty("赠送数量,赠几盒")
    @NotNull(message = "赠送数量,赠几盒_不能为空")
    private Long giveNumber;

    /**
     * b2b优惠券id
     */
    @ApiModelProperty("b2b优惠券id")
    @NotNull(message = "b2b优惠券id不能为空")
    private Long couponId;


}

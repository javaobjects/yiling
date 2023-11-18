package com.yiling.b2b.app.member.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * B2B移动端-获取会员实际支付金额 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-30
 */
@Data
@ApiModel
public class GetMemberOrderAmountForm extends BaseForm {

    /**
     * 购买条件ID
     */
    @NotNull
    @ApiModelProperty(value = "购买条件ID",required = true)
    private Long buyStageId;

    /**
     * 使用会员优惠券ID
     */
    @ApiModelProperty("使用会员优惠券ID")
    private Long couponId;

}

package com.yiling.b2b.app.member.form;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * B2B移动端-开通会员 Form
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/26
 */
@Data
@ApiModel
public class CreateMemberOrderForm extends BaseForm {

    /**
     * 购买条件ID
     */
    @NotNull
    @ApiModelProperty(value = "购买条件ID",required = true)
    private Long buyStageId;

    /**
     * 支付方式
     */
    @NotNull
    @Range(min = 1,max = 10)
    @ApiModelProperty(value = "支付方式",required = true)
    private Integer payMethod;

    /**
     * 支付金额
     */
    @NotNull
    @DecimalMin(value = "0.00", message = "实付金额必须大于0")
    @ApiModelProperty(value = "支付金额",required = true)
    private BigDecimal payAmount;

    /**
     * 推广方ID
     */
    @Min(1)
    @ApiModelProperty("推广方ID")
    private Long promoterId;

    /**
     * 推广人ID
     */
    @Min(1)
    @ApiModelProperty("推广人ID")
    private Long promoterUserId;

    /**
     * 使用会员优惠券ID
     */
    @ApiModelProperty("使用会员优惠券ID")
    private Long couponId;

}

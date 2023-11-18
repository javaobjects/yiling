package com.yiling.admin.b2b.member.form;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员确认退款 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-15
 */
@Data
@ApiModel("会员提交退款Form")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateMemberReturnForm extends BaseForm {

    /**
     * 购买记录ID
     */
    @NotNull
    @ApiModelProperty(value = "购买记录ID", required = true)
    private Long id;

    /**
     * 提交退款金额
     */
    @NotNull
    @DecimalMin(value = "0.01",message = "退款金额不能小于0")
    @ApiModelProperty(value = "退款金额", required = true)
    private BigDecimal submitReturnAmount;

    /**
     * 退款原因
     */
    @NotEmpty
    @Length(max = 50)
    @ApiModelProperty(value = "退款原因", required = true)
    private String returnReason;

    /**
     * 退款备注
     */
    @Length(max = 50)
    @ApiModelProperty("退款备注")
    private String returnRemark;
}

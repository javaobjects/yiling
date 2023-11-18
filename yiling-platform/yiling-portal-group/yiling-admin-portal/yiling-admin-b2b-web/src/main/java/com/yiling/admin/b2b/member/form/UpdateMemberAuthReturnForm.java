package com.yiling.admin.b2b.member.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员同意/驳回退款 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-18
 */
@Data
@ApiModel("会员同意/驳回退款Form")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateMemberAuthReturnForm extends BaseForm {

    /**
     * 退款ID
     */
    @NotNull
    @ApiModelProperty(value = "退款ID", required = true)
    private Long id;

    /**
     * 审核状态： 2-已审核 3-已驳回
     */
    @NotNull
    @Range(min = 2, max = 3)
    @ApiModelProperty(value = "审核状态： 2-已审核 3-已驳回", required = true)
    private Integer authStatus;

}

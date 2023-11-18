package com.yiling.admin.hmc.activity.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 患者审核入参
 * @author: fan.shen
 * @data: 2023-02-02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ActivityDocPatientAuditForm extends BaseForm {

    @NotNull
    @ApiModelProperty("id")
    private Integer id;

    @NotNull
    @ApiModelProperty("审核结果 1-通过 2-驳回")
    private Integer checkResult;

    @ApiModelProperty("驳回原因")
    private String rejectReason;

}

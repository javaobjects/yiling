package com.yiling.admin.hmc.welfare.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/27
 */
@Data
public class SaveDrugWelfareEnterpriseForm extends BaseForm {

    @NotNull
    @ApiModelProperty("商家id")
    private Long eid;

    @NotBlank
    @ApiModelProperty("商家名称")
    private String ename;

    @NotNull
    @ApiModelProperty("福利计划id")
    private Long drugWelfareId;

    @Length(max = 255)
    @ApiModelProperty("福利计划id")
    private String remake;
}

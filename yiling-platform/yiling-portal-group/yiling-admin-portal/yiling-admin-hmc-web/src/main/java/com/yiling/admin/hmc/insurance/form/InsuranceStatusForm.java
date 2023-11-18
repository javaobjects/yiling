package com.yiling.admin.hmc.insurance.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceStatusForm extends BaseForm {

    @ApiModelProperty("保险id")
    @NotNull(message = "保险未选中")
    private Long id;

    @ApiModelProperty("保险状态 1-启用 2-停用")
    @NotNull(message = "保险状态不能为空")
    @Max(value = 2, message = "保险状态不规范")
    @Min(value = 1, message = "保险状态不规范")
    private Integer status;
}

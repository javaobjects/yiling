package com.yiling.admin.sales.assistant.task.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 任务配送商移除
 * @author gxl
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteTaskDistributorForm extends BaseForm {

    @NotNull
    @ApiModelProperty(value = "任务配送商关联id，非任务id", required = true)
    private Long taskDistributorId;

}

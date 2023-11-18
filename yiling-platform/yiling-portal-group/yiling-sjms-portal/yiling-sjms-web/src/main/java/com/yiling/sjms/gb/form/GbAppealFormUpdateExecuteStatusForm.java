package com.yiling.sjms.gb.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/5/16
 */
@Data
public class GbAppealFormUpdateExecuteStatusForm extends BaseForm {

    /**
     * 团购处理列表ID
     */
    @NotNull
    @Min(value = 1, message = "团购处理列表ID最小为1")
    @ApiModelProperty(value = "团购处理列表ID")
    private Long appealFormId;

    /**
     * 处理状态:1-未开始、2-自动处理中、3-已处理、4-处理失败、5-手动处理中。数据字典：gb_appeal_form_data_exec_status
     */
    @NotNull
    @Min(value = 1, message = "处理状态范围1-5")
    @Max(value = 5, message = "处理状态范围1-5")
    @ApiModelProperty(value = "处理状态:1-未开始、2-自动处理中、3-已处理、4-处理失败、5-手动处理中。数据字典：gb_appeal_form_data_exec_status")
    private Integer dataExecStatus;

}

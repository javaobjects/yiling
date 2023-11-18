package com.yiling.sjms.gb.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/5/18
 */
@Data
public class GbAppealFormExecuteFlowWashForm extends BaseForm {

    /**
     * 团购处理列表ID
     */
    @NotNull
    @Min(1)
    @ApiModelProperty(value = "团购处理列表ID")
    private Long appealFormId;

    /**
     * 原流向ID
     */
    @NotNull
    @Min(1)
    @ApiModelProperty(value = "原流向ID")
    private Long flowWashId;

}

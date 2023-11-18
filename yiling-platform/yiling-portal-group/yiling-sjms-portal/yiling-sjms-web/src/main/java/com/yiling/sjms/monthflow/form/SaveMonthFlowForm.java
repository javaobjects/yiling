package com.yiling.sjms.monthflow.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: gxl
 * @date: 2023/6/25
 */
@Data
public class SaveMonthFlowForm extends BaseForm {

    /**
     * 批量添加请求体
     */
    @ApiModelProperty("上传")
    @NotEmpty
    private List<SaveSubForm> subForms;

    @ApiModelProperty("formId-不是第一次添加的时候带上")
    private Long formId;

    /**
     * 申诉类型 1补传月流向
     */
    @ApiModelProperty(value = "申诉类型 1补传月流向")
    private Integer appealType;
}

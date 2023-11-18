package com.yiling.sjms.gb.form;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/5/30
 */
@Data
public class SaveGbAppealFormSaleReportMatchForm extends BaseForm {

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
    @NotEmpty
    @ApiModelProperty(value = "原流向ID列表")
    private List<Long> flowWashIdList;

}

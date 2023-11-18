package com.yiling.sjms.gb.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/5/30
 */
@Data
public class QueryGbAppealFormAllocationDetailForm extends BaseForm {

    /**
     * 团购数据处理扣减/增加ID
     */
    @NotNull
    @Min(1)
    @ApiModelProperty(value = "团购数据处理扣减/增加ID")
    private Long appealAllocationId;
}

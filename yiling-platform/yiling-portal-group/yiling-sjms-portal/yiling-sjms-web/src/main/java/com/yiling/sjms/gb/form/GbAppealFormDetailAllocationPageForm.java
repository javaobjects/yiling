package com.yiling.sjms.gb.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/5/16
 */
@Data
public class GbAppealFormDetailAllocationPageForm extends QueryPageListForm {

    /**
     * 团购处理列表ID
     */
    @NotNull
    @Min(1)
    @ApiModelProperty(value = "团购处理列表ID")
    private Long appealFormId;

    /**
     * 分配类型:1-扣减 2-增加
     */
    @NotNull
    @Min(1)
    @Max(2)
    @ApiModelProperty(value = "分配类型:1-扣减 2-增加")
    private Integer allocationType;

}

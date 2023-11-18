package com.yiling.sjms.flee.form;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 选择流向提交审核
 * @author: xinxuan.jia
 * @date: 2023/6/26
 */
@Data
public class UpdateSelectAppealFlowForm {
    @ApiModelProperty("列表id")
    @NotNull(message = "列表id")
    private Long id;

}

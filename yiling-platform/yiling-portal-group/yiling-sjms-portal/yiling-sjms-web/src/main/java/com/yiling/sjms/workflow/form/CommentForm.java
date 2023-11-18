package com.yiling.sjms.workflow.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 批注 Form
 *
 * @author: xuan.zhou
 * @date: 2023/2/15
 */
@Data
public class CommentForm {

    @ApiModelProperty("转发历史记录ID")
    private Long forwardHistoryId;

    @NotNull
    @ApiModelProperty(value = "单据ID", required = true)
    private Long formId;

    @NotEmpty
    @ApiModelProperty(value = "批注内容", required = true)
    private String text;
}

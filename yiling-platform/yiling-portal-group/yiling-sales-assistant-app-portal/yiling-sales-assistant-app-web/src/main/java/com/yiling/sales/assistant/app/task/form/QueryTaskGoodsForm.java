package com.yiling.sales.assistant.app.task.form;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * @author: ray
 * @date: 2021/9/28
 */
@Data
@Accessors(chain = true)
public class QueryTaskGoodsForm {
    @ApiModelProperty(required = true)
    @NotNull
    private Long taskId;
}
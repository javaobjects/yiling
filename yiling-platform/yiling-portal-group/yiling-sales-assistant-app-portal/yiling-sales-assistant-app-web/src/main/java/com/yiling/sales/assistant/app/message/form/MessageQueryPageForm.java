package com.yiling.sales.assistant.app.message.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/9/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MessageQueryPageForm extends QueryPageListForm {
    @NotNull
    @ApiModelProperty(value = "0 所有，1 业务进度，2 发布任务")
    private Integer type;
}

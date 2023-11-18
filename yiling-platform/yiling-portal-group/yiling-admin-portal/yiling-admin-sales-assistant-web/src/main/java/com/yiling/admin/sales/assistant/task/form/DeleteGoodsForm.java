package com.yiling.admin.sales.assistant.task.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 移除任务商品
 * @author: hongyang.zhang
 * @data: 2021/09/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteGoodsForm extends BaseForm {

    @NotNull
    @ApiModelProperty(value = "任务与商品ID", required = true)
    private Long taskGoodsId;

}

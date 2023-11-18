package com.yiling.admin.sales.assistant.task.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 任务区域
 * @author: ray
 * @date: 2021/9/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddTaskAreaForm extends BaseForm {

    @ApiModelProperty(value = "父区域code")
    private String code;
    @ApiModelProperty(value = "子区域code集合")
    private List<String> children;
 /*   @ApiModelProperty(value = "子区域children")
    private List<AddTaskAreaForm> children;*/

   // private String name;
}
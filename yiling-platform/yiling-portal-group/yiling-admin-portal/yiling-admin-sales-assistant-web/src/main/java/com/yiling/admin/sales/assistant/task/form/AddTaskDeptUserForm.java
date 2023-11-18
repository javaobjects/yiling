package com.yiling.admin.sales.assistant.task.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/9/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddTaskDeptUserForm extends BaseForm {

    @ApiModelProperty(value = "部门人员id", required = true)
    private Long userId;

    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门id", required = true)
    private Long deptId;


}
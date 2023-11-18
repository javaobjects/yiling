package com.yiling.data.center.admin.system.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改部门状态 Form
 *
 * @author xuan.zhou
 * @date 2021/11/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateDepartmentStatusForm extends BaseForm {

    /**
     * 部门ID
     */
    @NotNull
    @ApiModelProperty(value = "部门ID", required = true)
    private Long id;

    /**
     * 状态：1-启用 2-停用
     */
    @NotNull
    @Range(min = 1, max = 2)
    @ApiModelProperty(value = "状态：1-启用 2-停用", required = true)
    private Integer status;
}

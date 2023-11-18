package com.yiling.data.center.admin.system.form;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业部门信息新增 Form
 * </p>
 *
 * @author gxl
 * @date 2021-05-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddDepartmentForm extends BaseForm {

    /**
     * 部门名称
     */
    @NotEmpty
    @Length(max = 50)
    @ApiModelProperty(value = "部门名称", required = true)
    private String name;

    /**
     * 部门编码
     */
    @Length(max = 50)
    @ApiModelProperty(value = "部门编码")
    private String code;

    /**
     * 部门描述
     */
    @Length(max = 200)
    @ApiModelProperty(value = "部门描述")
    private String description;

    /**
     * 上级部门ID
     */
    @ApiModelProperty(value = "上级部门ID")
    private Long parentId;

    /**
     * 部门负责人ID
     */
    @ApiModelProperty(value = "部门负责人ID")
    private Long managerId;

}

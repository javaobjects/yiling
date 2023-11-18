package com.yiling.data.center.admin.system.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改员工信息 Form
 *
 * @author: xuan.zhou
 * @date: 2021/5/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateEmployeeForm extends BaseForm {

    @NotNull(groups = { YilingEmployeeUpdateValidateGroup.class, CommonEmployeeUpdateValidateGroup.class })
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    @NotNull(groups = { YilingEmployeeUpdateValidateGroup.class, CommonEmployeeUpdateValidateGroup.class })
    @Length(max = 50, groups = { YilingEmployeeUpdateValidateGroup.class, CommonEmployeeUpdateValidateGroup.class }, message = "姓名长度不能超过50")
    @ApiModelProperty(value = "姓名", required = true)
    private String name;

    @NotEmpty(groups = { YilingEmployeeUpdateValidateGroup.class })
    @Length(max = 50, groups = { YilingEmployeeUpdateValidateGroup.class, CommonEmployeeUpdateValidateGroup.class }, message = "工号长度不能超过50")
    @ApiModelProperty(value = "工号")
    private String code;

    @NotNull(groups = { YilingEmployeeUpdateValidateGroup.class })
    @Range(min = 1, max = 100, groups = { YilingEmployeeUpdateValidateGroup.class, CommonEmployeeUpdateValidateGroup.class })
    @ApiModelProperty(value = "员工类型（数据字典：employee_type）")
    private Integer type;

    @NotNull(groups = { YilingEmployeeUpdateValidateGroup.class, CommonEmployeeUpdateValidateGroup.class })
    @ApiModelProperty(value = "角色ID", required = true)
    private Long roleId;

    @ApiModelProperty("职位ID")
    private Long positionId;

    @ApiModelProperty("上级领导ID")
    private Long parentId;

    @NotEmpty(groups = { YilingEmployeeUpdateValidateGroup.class, CommonEmployeeUpdateValidateGroup.class })
    @ApiModelProperty(value = "所属部门ID列表", required = true)
    private List<Long> departmentIds;

    public interface YilingEmployeeUpdateValidateGroup {
    }

    public interface CommonEmployeeUpdateValidateGroup {
    }

}

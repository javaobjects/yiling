package com.yiling.data.center.admin.system.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建员工信息 Form
 *
 * @author: xuan.zhou
 * @date: 2021/5/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateEmployeeForm extends BaseForm {

    @NotEmpty(groups = { YilingEmployeeCreateValidateGroup.class, CommonEmployeeCreateValidateGroup.class })
    @Length(max = 20, groups = { YilingEmployeeCreateValidateGroup.class, CommonEmployeeCreateValidateGroup.class })
    @ApiModelProperty(value = "姓名", required = true)
    private String name;

    @NotEmpty(groups = { YilingEmployeeCreateValidateGroup.class, CommonEmployeeCreateValidateGroup.class })
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "手机号格式不正确", groups = { YilingEmployeeCreateValidateGroup.class, CommonEmployeeCreateValidateGroup.class })
    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    @NotEmpty(groups = { YilingEmployeeCreateValidateGroup.class })
    @Length(max = 50, groups = { YilingEmployeeCreateValidateGroup.class, CommonEmployeeCreateValidateGroup.class }, message = "工号长度不能超过50")
    @ApiModelProperty(value = "工号")
    private String code;

    @NotNull(groups = { YilingEmployeeCreateValidateGroup.class })
    @Range(min = 1, max = 100, groups = { YilingEmployeeCreateValidateGroup.class, CommonEmployeeCreateValidateGroup.class })
    @ApiModelProperty(value = "员工类型（数据字典：employee_type）")
    private Integer type;

    @NotNull(groups = { YilingEmployeeCreateValidateGroup.class, CommonEmployeeCreateValidateGroup.class })
    @ApiModelProperty(value = "角色ID", required = true)
    private Long roleId;

    @ApiModelProperty("职位ID")
    private Long positionId;

    @ApiModelProperty("上级领导ID")
    private Long parentId;

    @NotEmpty(groups = { YilingEmployeeCreateValidateGroup.class, CommonEmployeeCreateValidateGroup.class })
    @ApiModelProperty(value = "所属部门ID列表", required = true)
    private List<Long> departmentIds;

    public interface YilingEmployeeCreateValidateGroup {
    }

    public interface CommonEmployeeCreateValidateGroup {
    }

}

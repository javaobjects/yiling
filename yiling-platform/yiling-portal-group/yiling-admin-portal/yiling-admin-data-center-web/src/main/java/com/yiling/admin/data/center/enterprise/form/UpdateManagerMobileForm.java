package com.yiling.admin.data.center.enterprise.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改企业管理员手机号 Form
 *
 * @author: xuan.zhou
 * @date: 2022/3/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateManagerMobileForm extends BaseForm {

    @NotNull
    @Min(1)
    @ApiModelProperty(value = "企业ID", required = true)
    private Long eid;

    @NotNull
    @Min(1)
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    @NotEmpty
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "^新手机号格式错误")
    @ApiModelProperty(value = "新手机号", required = true)
    private String newMobile;

    @NotEmpty
    @Length(min = 1, max = 10)
    @ApiModelProperty(value = "姓名", required = true)
    private String name;
}

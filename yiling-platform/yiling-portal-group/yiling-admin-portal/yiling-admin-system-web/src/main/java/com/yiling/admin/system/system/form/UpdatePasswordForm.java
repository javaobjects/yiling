package com.yiling.admin.system.system.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改密码 Form
 *
 * @author: lun.yu
 * @date: 2021/8/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdatePasswordForm extends BaseForm {

    /**
     * 原密码
     */
    @NotEmpty(message = "^请填写原密码")
    @ApiModelProperty(value = "原密码", required = true)
    private String oldPassword;

    /**
     * 新密码
     */
    @NotEmpty(message = "^请填写新密码")
    @Pattern(regexp = Constants.REGEXP_PASSWORD, message = "^请填写8-16位字母+数字组合")
    @ApiModelProperty(value = "新密码", required = true)
    private String password;

}

package com.yiling.b2b.app.system.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改密码 Form
 *
 * @author: xuan.zhou
 * @date: 2021/11/1
 */
@Data
public class UpdatePasswordForm {

    /**
     * 验证码
     */
    @NotEmpty(message = "^请填写验证码")
    @ApiModelProperty(value = "验证码", required = true)
    private String verifyCode;

    /**
     * 新密码
     */
    @NotEmpty(message = "^请填写新密码")
    @Pattern(regexp = Constants.REGEXP_PASSWORD, message = "^请填写8-16位字母+数字组合")
    @ApiModelProperty(value = "新密码", required = true)
    private String password;
}

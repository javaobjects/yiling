package com.yiling.admin.system.system.form;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录 Form
 *
 * @author: xuan.zhou
 * @date: 2021/5/13
 */
@Data
public class LoginForm {

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    @ApiModelProperty(value = "密码", required = true)
    private String password;

    /**
     * 验证码
     */
    @NotEmpty(message = "^验证码不能为空")
    @ApiModelProperty(value = "验证码", required = true)
    private String validateCode;

    /**
     * 验证码流水号
     */
    @NotEmpty(message = "^验证码流水号不能为空")
    @ApiModelProperty(value = "验证码流水号", required = true)
    private String serialNo;
}

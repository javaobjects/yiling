package com.yiling.user.web.auth.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * B2B PC 手机号+密码方式登录 Form
 *
 * @author xuan.zhou
 * @date 2022/4/7
 */
@Data
public class B2BPcPasswordLoginForm {

    /**
     * 手机号
     */
    @NotEmpty(message = "^请填写手机号")
    @Pattern(regexp = Constants.REGEXP_SPECIAL_MOBILE, message = "^手机号格式有误，请重新填写")
    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    /**
     * 密码
     */
    @NotEmpty(message = "^密码不能为空")
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

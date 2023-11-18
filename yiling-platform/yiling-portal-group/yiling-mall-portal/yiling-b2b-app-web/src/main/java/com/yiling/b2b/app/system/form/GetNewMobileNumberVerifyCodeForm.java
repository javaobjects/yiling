package com.yiling.b2b.app.system.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 获取更换绑定的新手机号短信验证码 Form
 *
 * @author: xuan.zhou
 * @date: 2021/8/10
 */
@Data
public class GetNewMobileNumberVerifyCodeForm {

    /**
     * 新手机号码
     */
    @NotEmpty(message = "^请填写手机号")
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "^手机号格式有误，请重新填写")
    @ApiModelProperty(value = "新手机号码", required = true)
    private String mobile;

    /**
     * 老手机号码的验证码
     */
    @NotEmpty(message = "^请填写验证码")
    @ApiModelProperty(value = "老手机号码的验证码", required = true)
    private String verifyCode;
}

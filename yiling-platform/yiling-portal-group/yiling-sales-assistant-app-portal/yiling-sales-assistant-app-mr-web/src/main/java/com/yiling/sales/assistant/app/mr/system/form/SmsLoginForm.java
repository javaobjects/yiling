package com.yiling.sales.assistant.app.mr.system.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 手机号+验证码方式登录 Form
 *
 * @author: xuan.zhou
 * @date: 2021/9/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SmsLoginForm extends BaseLoginForm {

    /**
     * 手机号
     */
    @NotEmpty(message = "^请填写手机号")
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "^手机号格式有误，请重新填写")
    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    /**
     * 短信验证码
     */
    @NotEmpty(message = "^短信验证码不能为空")
    @ApiModelProperty(value = "短信验证码", required = true)
    private String verifyCode;
}

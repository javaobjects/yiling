package com.yiling.sales.assistant.app.system.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 注册 Form
 *
 * @author: xuan.zhou
 * @date: 2021/9/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RegistForm extends BaseForm {

    /**
     * 手机号
     */
    @NotEmpty(message = "^请填写手机号")
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "^手机号格式有误，请重新填写")
    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    /**
     * 验证码token
     */
    @NotEmpty(message = "^验证码token不能为空")
    @ApiModelProperty(value = "验证码token", required = true)
    private String verifyCodeToken;

    /**
     * 密码
     */
    @NotEmpty(message = "^请填写密码")
    @Pattern(regexp = Constants.REGEXP_PASSWORD, message = "^请填写8-16位字母+数字组合")
    @ApiModelProperty(value = "密码", required = true)
    private String password;
}

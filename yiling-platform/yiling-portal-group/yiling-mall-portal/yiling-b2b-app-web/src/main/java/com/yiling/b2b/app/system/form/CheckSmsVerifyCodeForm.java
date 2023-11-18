package com.yiling.b2b.app.system.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 校验短信验证码 Form
 *
 * @author: xuan.zhou
 * @date: 2021/9/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CheckSmsVerifyCodeForm extends BaseForm {

    /**
     * 手机号
     */
    @NotEmpty(message = "^请填写手机号")
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "^手机号格式有误，请重新填写")
    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    /**
     * 验证码
     */
    @NotEmpty(message = "^请填写验证码")
    @ApiModelProperty(value = "验证码", required = true)
    private String verifyCode;

    /**
     * 新密码（新增的字段，为了APP版本兼容，接口暂不做必填校验）
     */
    @Pattern(regexp = Constants.REGEXP_PASSWORD, message = "^请填写8-16位字母+数字组合")
    @ApiModelProperty(value = "新密码")
    private String password;

    /**
     * 确认密码
     */
    @Pattern(regexp = Constants.REGEXP_PASSWORD, message = "^请填写8-16位字母+数字组合")
    @ApiModelProperty(value = "确认密码")
    private String rePassword;

}

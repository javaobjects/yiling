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
 * 校验更换绑定的新手机号短信验证码 Form
 *
 * @author: xuan.zhou
 * @date: 2021/8/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CheckNewMobileNumberVerifyCodeForm extends BaseForm {

    /**
     * 新手机号码
     */
    @NotEmpty(message = "^请填写新手机号码")
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "^新手机号码格式有误，请重新填写")
    @ApiModelProperty(value = "新手机号码", required = true)
    private String mobile;

    /**
     * 新手机号验证码
     */
    @NotEmpty(message = "^请填写新手机号验证码")
    @ApiModelProperty(value = "新手机号验证码", required = true)
    private String verifyCode;

    /**
     * 原手机号验证码
     */
    @NotEmpty(message = "^请填写原手机号验证码")
    @ApiModelProperty(value = "原手机号验证码", required = true)
    private String originalVerifyCode;

}

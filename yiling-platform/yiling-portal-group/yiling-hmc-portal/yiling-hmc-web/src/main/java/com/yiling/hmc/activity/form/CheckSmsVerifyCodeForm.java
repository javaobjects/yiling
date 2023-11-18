package com.yiling.hmc.activity.form;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.util.Constants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 校验短信验证码 Form
 *
 * @author: fan.shen
 * @date: 2022-09-07
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

}

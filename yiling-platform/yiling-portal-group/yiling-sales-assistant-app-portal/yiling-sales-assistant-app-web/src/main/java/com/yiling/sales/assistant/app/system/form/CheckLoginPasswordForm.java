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
 * 校验登录密码 Form
 *
 * @author: lun.yu
 * @date: 2022/1/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CheckLoginPasswordForm extends BaseForm {

    /**
     * 原手机号
     */
    @NotEmpty(message = "^请填写原手机号")
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "^手机号格式有误，请重新填写")
    @ApiModelProperty(value = "原手机号", required = true)
    private String oldMobile;

    /**
     * 登录密码
     */
    @NotEmpty(message = "^请填写登录密码")
    @ApiModelProperty(value = "登录密码", required = true)
    private String password;

}

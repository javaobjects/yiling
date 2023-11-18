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
 * 换绑手机号 Form
 *
 * @author: lun.yu
 * @date: 2021/9/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ResetMobileForm extends BaseForm {

    /**
     * 原手机号
     */
    @NotEmpty(message = "^请填写原手机号")
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "^手机号格式有误，请重新填写")
    @ApiModelProperty(value = "原手机号", required = true)
    private String oldMobile;

    /**
     * 新手机号
     */
    @NotEmpty(message = "^请填写新手机号")
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "^手机号格式有误，请重新填写")
    @ApiModelProperty(value = "新手机号", required = true)
    private String newMobile;

    /**
     * 新验证码
     */
    @NotEmpty(message = "^请填写新验证码")
    @ApiModelProperty(value = "新验证码", required = true)
    private String newVerifyCode;

}

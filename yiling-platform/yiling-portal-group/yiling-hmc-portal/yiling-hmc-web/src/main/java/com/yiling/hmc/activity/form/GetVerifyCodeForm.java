package com.yiling.hmc.activity.form;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.util.Constants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 获取验证码
 * @author: fan.shen
 * @date: 2022-09-07
 */
@Data
public class GetVerifyCodeForm extends BaseForm {

    @NotEmpty(message = "^请填写手机号")
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "^手机号格式有误，请重新填写")
    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

}
package com.yiling.b2b.app.system.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 获取特殊号码换绑新手机号短信验证码 Form
 *
 * @author: lun.yu
 * @date: 2021/12/17
 */
@Data
public class GetSpecialMobileVerifyCodeForm {

    /**
     * 新手机号码
     */
    @NotEmpty(message = "^请填写手机号")
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "^手机号格式有误，请重新填写")
    @ApiModelProperty(value = "新手机号码", required = true)
    private String mobile;

}

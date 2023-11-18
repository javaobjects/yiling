package com.yiling.user.web.auth.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * B2B APP手机号+密码方式登录 Form
 *
 * @author xuan.zhou
 * @date 2022/4/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class B2BAppPasswordLoginForm extends BaseAppLoginForm {

    /**
     * 手机号
     */
    @NotEmpty(message = "^请填写手机号")
    @Pattern(regexp = Constants.REGEXP_SPECIAL_MOBILE, message = "^您填写的手机号格式有误，请重新填写")
    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    /**
     * 密码
     */
    @NotEmpty(message = "^密码不能为空")
    @ApiModelProperty(value = "密码", required = true)
    private String password;

}

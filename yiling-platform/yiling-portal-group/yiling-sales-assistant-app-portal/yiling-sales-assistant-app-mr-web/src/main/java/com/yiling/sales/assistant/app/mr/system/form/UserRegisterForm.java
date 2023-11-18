package com.yiling.sales.assistant.app.mr.system.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.util.Constants;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户注册 Form
 *
 * @author: xuan.zhou
 * @date: 2023/2/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserRegisterForm extends BaseForm {

    /**
     * 手机号
     */
    @NotEmpty(message = "^请填写手机号")
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "^手机号格式有误，请重新填写")
    private String mobile;

    /**
     * 密码
     */
    @NotEmpty
    private String password;

    /**
     * 所属企业名称
     */
    @NotEmpty
    private String ename;
}

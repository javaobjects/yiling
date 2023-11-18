package com.yiling.sales.assistant.app.userteam.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**

 * @author: lun.yu
 * @date: 2021/9/26
 */
@Data
@ApiModel
public class InviteMemberForm extends BaseForm {

    @NotEmpty
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "手机号错误，请重新输入")
    private String mobilePhone;

}

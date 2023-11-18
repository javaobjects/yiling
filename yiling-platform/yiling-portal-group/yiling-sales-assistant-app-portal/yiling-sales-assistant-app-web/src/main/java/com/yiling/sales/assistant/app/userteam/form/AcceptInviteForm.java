package com.yiling.sales.assistant.app.userteam.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 确认邀请 Form
 * @author: lun.yu
 * @date: 2022/01/11
 */
@Data
@ApiModel
public class AcceptInviteForm extends BaseForm {

    @NotEmpty
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "手机号错误，请重新输入")
    @ApiModelProperty(value = "手机号", required = true)
    private String mobilePhone;

    @NotEmpty(message = "^短信验证码不能为空")
    @ApiModelProperty(value = "短信验证码", required = true)
    private String verifyCode;

    @NotNull
    @Min(1)
    @ApiModelProperty(value = "邀请人ID",required = true)
    private Long parentId;

    @NotNull
    @Range(min = 1,max = 2)
    @ApiModelProperty(value = "邀请方式：1-短信 2-微信")
    private Integer inviteType;

}

package com.yiling.sales.assistant.app.system.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 申请注销账号 Form
 *
 * @author lun.yu
 * @date 2022-06-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddDeregisterAccountForm extends BaseForm {

    @NotEmpty
    @ApiModelProperty(value = "验证码", required = true)
    private String verifyCode;

    @NotNull
    @ApiModelProperty(value = "终端类型：1-Android 2-iOS", required = true)
    private Integer terminalType;

    /**
     * 注销原因
     */
    @Length(max = 50)
    @ApiModelProperty(value = "注销原因")
    private String applyReason;

}

package com.yiling.sales.assistant.app.payment.form;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GetWechatOpenIdForm extends BaseForm {

    @NotEmpty
    @ApiModelProperty(value = "code")
    private String code;
}

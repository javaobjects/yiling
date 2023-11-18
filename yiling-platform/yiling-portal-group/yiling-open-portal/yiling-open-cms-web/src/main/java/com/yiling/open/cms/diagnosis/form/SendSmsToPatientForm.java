package com.yiling.open.cms.diagnosis.form;


import com.yiling.basic.sms.enums.SmsTypeEnum;
import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.open.cms.diagnosis.enums.RemindPatientSmsTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 获取文件 form
 * @author: fan.shen
 * @date: 2022/9/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SendSmsToPatientForm extends BaseForm {

    @NotBlank
    @ApiModelProperty("手机号")
    String mobile;

    @NotBlank
    @ApiModelProperty("内容")
    String content;

    @NotNull
    @ApiModelProperty("短信类型")
    RemindPatientSmsTypeEnum typeEnum;


}
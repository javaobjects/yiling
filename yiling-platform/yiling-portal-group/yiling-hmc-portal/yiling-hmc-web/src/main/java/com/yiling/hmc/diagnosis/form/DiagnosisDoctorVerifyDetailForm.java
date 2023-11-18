package com.yiling.hmc.diagnosis.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 已认证详情 form
 *
 * @author: fan.shen
 * @date: 2024/5/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DiagnosisDoctorVerifyDetailForm extends BaseForm {

    @NotNull
    @ApiModelProperty(value = "医生id")
    private Integer doctorId;

}
package com.yiling.hmc.diagnosis.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 获取就诊人详情 form
 *
 * @author: fan.shen
 * @date: 2024/5/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GetPatientDetailForm extends BaseForm {

    @NotNull
    @ApiModelProperty(value = "患者id")
    private Integer patientId;

}
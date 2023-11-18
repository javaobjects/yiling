package com.yiling.hmc.diagnosis.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 问诊单详情入参
 *
 * @author: fan.shen
 * @data: 2023/05/15
 */
@Data
@Accessors(chain = true)
public class HmcSelectDiagnosisRecordInfoForm {

    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "问诊单id")
    private Integer diagnosisRecordId;


}

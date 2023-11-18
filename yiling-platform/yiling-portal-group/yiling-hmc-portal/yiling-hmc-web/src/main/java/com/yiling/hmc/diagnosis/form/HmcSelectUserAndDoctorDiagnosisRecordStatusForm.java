package com.yiling.hmc.diagnosis.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 查询用户、患者、医生之间有没有进行中、未开始的问诊单
 * @author: fan.shen
 * @data: 2023/06/01
 */
@Data
@Accessors(chain = true)
public class HmcSelectUserAndDoctorDiagnosisRecordStatusForm {


    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "hmc用户id")
    private Integer fromUserId;

    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "患者id")
    private Integer patientId;

    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "医生id")
    private Integer docId;

}

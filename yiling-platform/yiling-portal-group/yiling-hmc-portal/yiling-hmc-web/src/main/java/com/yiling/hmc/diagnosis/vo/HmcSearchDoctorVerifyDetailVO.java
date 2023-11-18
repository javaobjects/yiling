package com.yiling.hmc.diagnosis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 问诊医生 已认证详情
 * @author: fan.shen
 * @data: 2023/05/09
 */
@Data
@Accessors(chain = true)
public class HmcSearchDoctorVerifyDetailVO {

    @ApiModelProperty(value = "医生id")
    private Integer doctorId;

    @ApiModelProperty(value = "执业证书编号")
    private String doctorsNoteCertNo;

    @ApiModelProperty(value = "医师资格证书编号")
    private String certificateCertNo;


}

package com.yiling.hmc.diagnosis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * HMC医生号源所属时段VO
 * @author: fan.shen
 * @data: 2023/05/09
 */
@Data
public class HmcDoctorSignalSourceBelongTimeVO {

    @ApiModelProperty("所属时段")
    private String belongTime;

    @ApiModelProperty("预约状态 1：约满 0：未约满")
    private Integer appointmentState;
}

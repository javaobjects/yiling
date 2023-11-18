package com.yiling.hmc.activity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 患者信息
 *
 * @author: fan.shen
 * @date: 2022/9/7
 */
@Data
public class PatientVO implements java.io.Serializable {

    /**
     * 患者名称
     */
    @ApiModelProperty(value = "患者名称")
    private String patientName;

    /**
     * 患者年龄
     */
    @ApiModelProperty(value = "患者年龄")
    private Integer patientAge;

    /**
     * 患者性别
     */
    @ApiModelProperty(value = "患者性别 1-男，0-女 ")
    private Integer gender;

}

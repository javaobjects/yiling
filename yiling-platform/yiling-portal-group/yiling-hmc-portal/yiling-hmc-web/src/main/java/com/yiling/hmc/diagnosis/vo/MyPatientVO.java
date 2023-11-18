package com.yiling.hmc.diagnosis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 我的就诊人
 *
 * @author: fan.shen
 * @date: 2023-12-27
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "我的就诊人")
public class MyPatientVO {

    private static final long serialVersionUID = -333710312121608L;

    @ApiModelProperty("患者id")
    private Integer patientId;

    @ApiModelProperty("患者姓名")
    private String patientName;

    @ApiModelProperty("患者年龄")
    private Integer patientAge;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty(value = "身份证", hidden = true)
    private String idCard;

    @ApiModelProperty(value = "家庭关系")
    private String relation;

    @ApiModelProperty(value = "实名认证标志 1-是，0-否")
    private Integer realNameFlag;

    @ApiModelProperty(value = "是否完善信息 1：已完善 2：未完善")
    private Integer completeInfo;

}

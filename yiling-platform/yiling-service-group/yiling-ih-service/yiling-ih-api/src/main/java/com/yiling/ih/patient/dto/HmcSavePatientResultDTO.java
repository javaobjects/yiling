package com.yiling.ih.patient.dto;


import lombok.Data;

import java.util.List;


/**
 * 我的处方 DTO
 *
 * @author: fan.shen
 * @data: 2023/05/09
 */
@Data
public class HmcSavePatientResultDTO implements java.io.Serializable {

    private static final long serialVersionUID = 3768586786173659462L;

    // @ApiModelProperty("patientId")
    private Integer patientId;

    /**
     * 患者名称
     */
    // @ApiModelProperty(value = "患者名称")
    private String patientName;


    /**
     * 身份证号码
     */
    // @ApiModelProperty(value = "身份证")
    private String idCard;

    /**
     * 患者年龄
     */
    // @ApiModelProperty(value = "患者年龄")
    private Integer patientAge;

    /**
     * 患者性别
     */
    // @ApiModelProperty(value = " 性别1 : 男 ， 0 : 女")
    private Integer gender;

    /**
     * 手机号
     */
    // @ApiModelProperty(value = "手机号")
    private String mobile;

}

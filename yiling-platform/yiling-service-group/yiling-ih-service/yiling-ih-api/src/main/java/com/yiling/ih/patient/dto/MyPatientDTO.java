package com.yiling.ih.patient.dto;

import lombok.Data;

/**
 * 我的就诊人对象
 *
 * @author: fan.shen
 * @date: 2023-12-27
 */
@Data
public class MyPatientDTO implements java.io.Serializable {

    private static final long serialVersionUID = -333710312121608L;

    /**
     * 患者id
     */
    private Integer patientId;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 患者年龄
     */
    private Integer patientAge;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 家庭关系
     */
    private String relation;

    /**
     * 实名认证标志 1-是，0-否
     */
    private Integer realNameFlag;

    /**
     * 是否完善信息 1：已完善 2：未完善
     */
    private Integer completeInfo;

}

package com.yiling.ih.patient.dto;

import lombok.Data;

import java.util.List;

/**
 * 我的就诊人详情对象
 *
 * @author: fan.shen
 * @date: 2023-12-27
 */
@Data
public class MyPatientDetailDTO implements java.io.Serializable {

    private static final long serialVersionUID = -333710312121608L;

    /**
     * ID
     */
    private Integer id;

    /**
     * 患者名称
     */
    private String patientName;


    /**
     * 身份证号码
     */
    private String idCard;

    /**
     * 患者年龄
     */
    private Integer patientAge;

    /**
     * 患者性别1 : 男  0 : 女
     */
    private Integer gender;

    /**
     * 省code
     */
    private String provinceCode;

    /**
     * 市code
     */
    private String cityCode;

    /**
     * 区code
     */
    private String regionCode;

    /**
     * 省Name
     */
    private String provinceName;

    /**
     * 市Name
     */
    private String cityName;

    /**
     * 区Name
     */
    private String regionName;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 地址类型代码
     */
    private String addressTypeCode;

    /**
     * 家庭关系
     */
    private String relation;

    /**
     * 民族
     */
    private String nation;

    /**
     * 民族代码
     */
    private String nationCode;

    /**
     * 过往病史
     */
    private List<String> historyDisease;

    /**
     * 家族病史
     */
    private List<String> familyDisease;

    /**
     * 过敏史
     */
    private List<String> allergyHistory;

    /**
     * 实名认证标志 1-是，0-否
     */
    private Integer realNameFlag;

    /**
     * 是否完善信息 1：已完善 2：未完善
     */
    private Integer completeInfo;

}

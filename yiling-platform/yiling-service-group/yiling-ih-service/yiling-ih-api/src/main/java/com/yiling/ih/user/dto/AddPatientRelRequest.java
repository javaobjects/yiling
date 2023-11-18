package com.yiling.ih.user.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 保存患者信息 Request
 *
 * @author: fan.shen
 * @date: 2022-09-13
 */
@Data
@Accessors(chain = true)
public class AddPatientRelRequest implements java.io.Serializable  {

    /**
     * 患者名称
     */
    private String patientName;

    /**
     * 用户id
     */
    private Integer fromUserId;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 手机号
     */
    private String mobile;

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
     * 详细地址
     */
    private String address;

    /**
     * 地址类型代码
     */
    private String addressTypeCode;

    /**
     * 民族
     */
    private String nation;

    /**
     * 民族代码
     */
    private String nationCode;

    /**
     * 家庭关系,传code码（例：本人传01）
     */
    private String relation;

    /**
     * 医生id
     */
    private Integer doctorId;

    /**
     * 标签:所患疾病，所用药品
     */
    private List<PatientTagRequest> patientTagForms;

    /**
     * 描述
     */
    private String desc;

    /**
     * 就诊日期
     */
    private Date visitDate;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 实名认证标志 1-是，0-否
     */
    private Integer realNameFlag;
}

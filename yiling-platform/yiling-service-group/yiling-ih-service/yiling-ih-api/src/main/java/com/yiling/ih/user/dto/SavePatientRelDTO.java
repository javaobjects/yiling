package com.yiling.ih.user.dto;

import lombok.Data;

/**
 * 保存医患关系信息 DTO
 *
 * @author: fan.shen
 * @date: 2022-09-09
 */
@Data
public class SavePatientRelDTO implements java.io.Serializable {

    private static final long serialVersionUID = -2295160299359559926L;

    /**
     * 患者id
     */
    private Integer patientId;

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
     * 患者性别
     */
    private Integer gender;

    /**
     * 手机号
     */
    private String mobile;
}

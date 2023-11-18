package com.yiling.ih.user.dto;

import lombok.Data;

/**
 * 患者信息 DTO
 *
 * @author: fan.shen
 * @date: 2022/9/7
 */
@Data
public class PatientDTO implements java.io.Serializable {

    /**
     * 患者名称
     */
    private String patientName;

    /**
     * 患者年龄
     */
    private Integer patientAge;

    /**
     * 患者性别
     */
    private Integer gender;

}

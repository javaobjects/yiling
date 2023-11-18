package com.yiling.ih.user.dto;

import lombok.Data;

/**
 * 查询是否入组 DTO
 *
 * @author: fan.shen
 * @date: 2022/9/7
 */
@Data
public class PatientDoctorRelDTO implements java.io.Serializable {

    /**
     * 是否加入 true - 是，false - 否
     */
    private Boolean hasJoin;

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

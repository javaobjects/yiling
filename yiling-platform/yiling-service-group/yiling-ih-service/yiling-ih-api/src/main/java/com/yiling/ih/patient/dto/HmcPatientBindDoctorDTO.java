package com.yiling.ih.patient.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 患者DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HmcPatientBindDoctorDTO extends BaseDTO {

    private static final long serialVersionUID = 3768586786173659462L;

    /**
     * 医生ID
     */
    private Integer doctorId;

    /**
     * 医生姓名
     */
    private String doctorName;

    /**
     * 第一执业医院
     */
    private String hospitalName;

    /**
     * 绑定来源
     */
    private String sourceType;

    /**
     * 绑定业务线
     */
    private String businessType;

    /**
     * 绑定时间
     */
    private Date createTime;

}
package com.yiling.ih.patient.dto;


import lombok.Data;

/**
 * HMC医生号源所属时段DTO
 * @author: fan.shen
 * @data: 2023/05/09
 */
@Data
public class HmcDoctorSignalSourceBelongTimeDTO implements java.io.Serializable {

    /**
     * 所属时段
     */
    private String belongTime;

    /**
     * 预约状态 1：约满 0：未约满
     */
    private Integer appointmentState;
}

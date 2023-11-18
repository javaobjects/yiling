package com.yiling.ih.patient.dto.request;

import lombok.Data;

/**
 * 查询患者绑定医生入参
 *
 * @author fan.shen
 * @date 2023-02-07
 */
@Data
public class QueryPatientBindDoctorRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * 就诊人id
     */
    private Integer patientId;

}

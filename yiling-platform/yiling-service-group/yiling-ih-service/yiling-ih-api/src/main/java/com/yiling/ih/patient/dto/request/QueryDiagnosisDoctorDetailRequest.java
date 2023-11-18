package com.yiling.ih.patient.dto.request;

import lombok.Data;

/**
 * 查询问诊医生详情
 *
 * @author fan.shen
 * @date 2023-02-07
 */
@Data
public class QueryDiagnosisDoctorDetailRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * 医生id
     */
    private Integer doctorId;

    private Integer fromUserId;


}

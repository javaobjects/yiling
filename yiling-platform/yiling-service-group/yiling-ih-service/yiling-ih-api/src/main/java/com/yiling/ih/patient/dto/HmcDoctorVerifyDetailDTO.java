package com.yiling.ih.patient.dto;


import lombok.Data;

import java.util.List;

/**
 * 医生已认证详情 DTO
 *
 * @author: fan.shen
 * @data: 2023/05/09
 */
@Data
public class HmcDoctorVerifyDetailDTO implements java.io.Serializable {

    /**
     * 医生id
     */
    private Integer doctorId;

    /**
     * 执业证书编号
     */
    private String doctorsNoteCertNo;

    /**
     * 医师资格证书编号
     */
    private String certificateCertNo;

}

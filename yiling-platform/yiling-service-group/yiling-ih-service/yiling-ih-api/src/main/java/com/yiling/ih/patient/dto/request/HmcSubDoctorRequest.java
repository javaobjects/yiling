package com.yiling.ih.patient.dto.request;

import lombok.Data;

/**
 * 关注、取关医生
 *
 * @author fan.shen
 * @date 2022/8/23
 */
@Data
public class HmcSubDoctorRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    private Integer fromUserId;

    private Integer doctorId;

}

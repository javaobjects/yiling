package com.yiling.ih.patient.dto;


import lombok.Data;

import java.util.List;


/**
 * 找医生DTO
 *
 * @author: fan.shen
 * @data: 2023/05/09
 */
@Data
public class HmcSearchDoctorResultDTO implements java.io.Serializable {

    private static final long serialVersionUID = 3768586786173659462L;

    private Long total;

    private List<HmcSearchDoctorDTO> list;

}

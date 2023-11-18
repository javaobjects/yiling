package com.yiling.ih.patient.dto;


import lombok.Data;

import java.util.List;


/**
 * 我的处方 DTO
 *
 * @author: fan.shen
 * @data: 2023/05/09
 */
@Data
public class HmcMyPrescriptionResultDTO implements java.io.Serializable {

    private static final long serialVersionUID = 3768586786173659462L;

    private Long total;

    private List<HmcMyPrescriptionDTO> list;

}

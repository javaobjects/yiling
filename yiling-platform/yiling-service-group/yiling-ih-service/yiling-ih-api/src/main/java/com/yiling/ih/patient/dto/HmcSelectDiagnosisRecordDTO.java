package com.yiling.ih.patient.dto;


import lombok.Data;

import java.util.List;


/**
 * 问诊列表DTO
 *
 * @author: fan.shen
 * @data: 2023/05/09
 */
@Data
public class HmcSelectDiagnosisRecordDTO implements java.io.Serializable {

    private static final long serialVersionUID = 3768586786173659462L;

    private Long total;

    private List<HmcSelectDiagnosisRecordListDTO> list;

}

package com.yiling.ih.patient.dto.request;

import lombok.Data;

import java.util.List;

/**
 * 批量查询问诊单 request
 *
 * @author fan.shen
 * @date 2023-05-18
 */
@Data
public class HmcDiagnosisOrderRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * 问诊单id
     */
    private List<Long> diagnosisRecordIdList;

}

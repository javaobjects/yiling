package com.yiling.ih.patient.dto.request;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 问诊单详情 request
 *
 * @author fan.shen
 * @date 2023-05-15
 */
@Data
public class HmcDiagnosisOrderDetailRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * 问诊单id
     */
    private Integer diagnosisRecordId;

}

package com.yiling.ih.patient.dto.request;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 查看评价
 *
 * @author fan.shen
 * @date 2023-02-07
 */
@Data
public class HmcQueryDiagnosisOrderCommentRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * 问诊单id
     */
    private Integer diagnosisRecordId;

}

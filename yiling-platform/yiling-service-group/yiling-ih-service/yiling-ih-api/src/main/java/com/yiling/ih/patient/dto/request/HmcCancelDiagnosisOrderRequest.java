package com.yiling.ih.patient.dto.request;

import lombok.Data;

/**
 * 取消咨询 request
 *
 * @author fan.shen
 * @date 2023-05-15
 */
@Data
public class HmcCancelDiagnosisOrderRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * 用户id
     */
    private Integer fromUserId;

    /**
     * 问诊单id
     */
    private Integer diagnosisRecordId;

}

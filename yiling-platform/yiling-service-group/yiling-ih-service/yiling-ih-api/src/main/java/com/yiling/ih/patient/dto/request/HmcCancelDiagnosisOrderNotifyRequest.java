package com.yiling.ih.patient.dto.request;

import lombok.Data;

/**
 * 取消咨询回调 request
 *
 * @author fan.shen
 * @date 2023-05-15
 */
@Data
public class HmcCancelDiagnosisOrderNotifyRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * 问诊单id
     */
    private Integer diagnosisRecordId;

    /**
     * 	退款记录id
     */
    private Integer diagnosisRecordRefundId;

    /**
     * 退款状态 0成功 1失败
     */
    private Integer cancelStatus;

    /**
     * 交行内部订单号
     */
    private String sysOrderNo;

}

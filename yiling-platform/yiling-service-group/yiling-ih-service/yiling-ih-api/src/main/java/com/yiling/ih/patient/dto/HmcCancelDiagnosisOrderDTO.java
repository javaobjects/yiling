package com.yiling.ih.patient.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 取消咨询DTO
 *
 * @author fan.shen
 * @date 2023-05-15
 */
@Data
public class HmcCancelDiagnosisOrderDTO implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * 状态 1问诊单不存在 2问诊单状态异常无法取消 3问诊单错误无法取消 4成功 5零元问诊单取消咨询成功
     */
    private Integer status;

    /**
     * 商户交易编号
     */
    private String merTranNo;

    /**
     * 	退款记录id
     */
    private Integer diagnosisRecordRefundId;

    /**
     * 	退款金额 单位元
     */
    private BigDecimal price;

    /**
     * 订单编号
     */
    private String diagnosisRecordOrderNo;

}

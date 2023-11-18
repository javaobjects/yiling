package com.yiling.ih.patient.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * HMC创建问诊订单VO
 *
 * @author: fan.shen
 * @data: 2023/05/12
 */
@Data
public class HmcSubmitDiagnosisOrderDTO implements java.io.Serializable {
    
    /**
     * 状态 1有正在进行的问诊单,diagnosisRecordId是正在进行的问诊单id 2所选号段已无号源的校验 3正常下单
     */
    private Integer status;

    /**
     * 问诊单id
     */
    private Integer diagnosisRecordId;

    /**
     * 单号
     */
    private String diagnosisRecordOrderNo;

    /**
     * 问诊单价格
     */
    private BigDecimal diagnosisRecordPrice;

}

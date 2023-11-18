package com.yiling.ih.patient.dto.request;

import lombok.Data;


/**
 * 问诊订单支付成功通知IH入参
 *
 * @author fan.shen
 * @date 2022/8/23
 */
@Data
public class DiagnosisOrderPaySuccessNotifyRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * 问诊单id
     */
    private Integer diagnosisRecordId;

    /**
     * 商户交易编号
     */
    private String merTranNo;

    /**
     * 银行端交易流水
     */
    private String bankTranNo;

    /**
     * 商户订单总金额
     */
    private String totalAmount;

    /**
     * 买家实付金额
     */
    private String buyerPayAmount;

    /**
     * 第三方渠道交易流水号
     */
    private String thirdPartyTranNo;

    /**
     * groupId
     */
    private String groupId;


}

package com.yiling.ih.patient.dto.request;

import lombok.Data;


/**
 * 处方订单支付成功入参
 *
 * @author fan.shen
 * @date 2022/8/23
 */
@Data
public class HmcPrescriptionOrderPaySuccessNotifyRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * IH处方订单id
     */
    private Integer orderId;

    /**
     * hmc商户单号
     */
    private String hmcOrderNo;

    /**
     * 第三方订单号 微信订单号-交行订单号
     */
    private String outOrderNo;

    /**
     * 第三方渠道交易流水号
     */
    private String thirdPartyTranNo;

}

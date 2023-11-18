package com.yiling.ih.patient.dto.request;

import lombok.Data;


/**
 * 取消处方订单入参
 *
 * @author fan.shen
 * @date 2022/8/23
 */
@Data
public class HmcCancelPrescriptionOrderRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * 订单id
     */
    private Integer orderId;

}

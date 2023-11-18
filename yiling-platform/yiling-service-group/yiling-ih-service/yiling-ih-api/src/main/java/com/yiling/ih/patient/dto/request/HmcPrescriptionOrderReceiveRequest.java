package com.yiling.ih.patient.dto.request;

import lombok.Data;


/**
 * 处方订单签收入参
 *
 * @author fan.shen
 * @date 2022/8/23
 */
@Data
public class HmcPrescriptionOrderReceiveRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * IH处方订单id
     */
    private Integer orderId;

    /**
     * 用户id
     */
    private Integer fromUserId;


}

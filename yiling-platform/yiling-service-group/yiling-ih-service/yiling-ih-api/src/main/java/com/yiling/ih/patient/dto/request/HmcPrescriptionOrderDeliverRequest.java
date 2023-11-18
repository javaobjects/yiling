package com.yiling.ih.patient.dto.request;

import lombok.Data;


/**
 * 处方订单发货入参
 *
 * @author fan.shen
 * @date 2022/8/23
 */
@Data
public class HmcPrescriptionOrderDeliverRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * IH处方订单id
     */
    private Integer orderId;

    /**
     * 快递公司
     */
    private String packageCompany;

    /**
     * 快递单号
     */
    private String packageNumber;
    /**
     * 操作人id
     */
    private Long userId;

}

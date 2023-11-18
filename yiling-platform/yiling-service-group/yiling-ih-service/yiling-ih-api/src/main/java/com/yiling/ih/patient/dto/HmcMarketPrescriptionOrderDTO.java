package com.yiling.ih.patient.dto;

import lombok.Data;

/**
 * 处方订单DTO
 *
 * @author: fan.shen
 * @date: 2023-12-27
 */
@Data
public class HmcMarketPrescriptionOrderDTO implements java.io.Serializable {

    private static final long serialVersionUID = -333710312121608L;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 处方编号
     */
    private String prescriptionNo;

    /**
     * 订单编号
     */
    private String prescriptionOrderNo;

}

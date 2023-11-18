package com.yiling.ih.patient.dto.request;

import lombok.Data;


/**
 * 处方订单入参
 *
 * @author fan.shen
 * @date 2022/8/23
 */
@Data
public class HmcCreateMarketPrescriptionOrderRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * 处方id
     */
    private Integer prescriptionId;

    /**
     *处方价格
     */
    private String prescriptionPrice;

    /**
     *收货地址id
     */
    private Integer addressId;

    /**
     *收货人姓名
     */
    private String addressName;

    /**
     *收货人手机号
     */
    private String addressMobile;

    /**
     *收货人地址
     */
    private String addressArea;

    /**
     *IH发货方id
     */
    private Integer deliveryPharmacyId;

    /**
     *备注
     */
    private String note;

    /**
     *用户id
     */
    private Integer fromUserId;
}

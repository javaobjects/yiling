package com.yiling.order.order.dto.request;

import java.util.Date;

import lombok.Data;

/**
 * 发货批次明细Request
 *
 * @author:wei.wang
 * @date:2021/6/25
 */
@Data
public class DeliveryInfoRequest implements java.io.Serializable {
    /**
     * 批次号
     */
    private String batchNo;


    /**
     * 有效期
     */
    private Date expiryDate;

    /**
     * 发货数量
     */
    private Integer deliveryQuantity;

    /**
     * ERP出库单号
     */
    private String erpDeliveryNo;

    /**
     * 生产日期
     */
    private Date produceDate;

    /**
     * eas出库单主键
     */
    private String easSendOrderId;

}

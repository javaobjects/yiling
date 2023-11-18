package com.yiling.order.order.dto.request;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

/**
 * 使用票折明细信息
 * @author:wei.wang
 * @date:2021/7/2
 */
@Data
public class OrderDetailTicketDiscountRequest implements java.io.Serializable {

    /**
     * 详情id
     */
    private Long detailId;

    /**
     * 商品goodsId
     */
    private Long goodsId;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    /**
     * 票折方式：1-按比率 2-按金额
     */
    private Integer invoiceDiscountType;

    /**
     * 票折折扣比率
     */
    private BigDecimal ticketDiscountRate;

    /**
     * 票折折扣金额
     */
    private BigDecimal ticketDiscountAmount;

    /**
     * 现折扣金额
     */
    private BigDecimal cashDiscountAmount;

    /**
     * 出库单号
     */
    private String erpDeliveryNo;

    /**
     * 开票金额小计
     */
    private BigDecimal invoiceAllAmount;

    /**
     * 开票数量
     */
    private Integer invoiceAllQuantity;

    /**
     * 批次信息
     */
    private List< SaveOrderTicketBatchRequest> saveOrderTicketBatchList;
}

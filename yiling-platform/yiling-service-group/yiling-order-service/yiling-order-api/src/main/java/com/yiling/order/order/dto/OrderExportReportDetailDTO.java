package com.yiling.order.order.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 导出报表DTO
 */
@Data
public class OrderExportReportDetailDTO implements java.io.Serializable {

    /**
     * 省份
     */
    private String provinceName;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 付款金额
     */
    private BigDecimal paymentAmount;
}

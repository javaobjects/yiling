package com.yiling.order.order.dto.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 开票批次信息
 * @author:wei.wang
 * @date:2021/12/29
 */
@Data
public class SaveOrderTicketBatchRequest implements java.io.Serializable  {

    /**
     * 批次编号
     */

    private String batchNo;

    /**
     * 开票数量
     */
    private Integer invoiceQuantity;

    /**
     * 现折金额
     */
    private BigDecimal cashDiscountAmount;

    /**
     * 票折金额
     */
    private BigDecimal ticketDiscountAmount;

    /**
     * 当前开票小计
     */
    private BigDecimal invoiceAmount;
}

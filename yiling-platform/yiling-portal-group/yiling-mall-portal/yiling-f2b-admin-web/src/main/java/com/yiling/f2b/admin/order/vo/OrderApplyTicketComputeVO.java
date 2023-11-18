package com.yiling.f2b.admin.order.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 申请票折计算结果
 * @author:wei.wang
 * @date:2021/8/11
 */
@Data
public class OrderApplyTicketComputeVO {

    /**
     * 票折金额
     */
    @ApiModelProperty(value = "票折金额")
    private BigDecimal ticketDiscountAmount;

    /**
     * 票折比率
     */
    @ApiModelProperty(value = "票折比率")
    private BigDecimal ticketDiscountRate;

    /**
     * 折扣比率
     */
    @ApiModelProperty(value = "折扣比率")
    private BigDecimal goodsDiscountRate;

    /**
     * 折扣金额
     */
    @ApiModelProperty(value = "折扣金额")
    private BigDecimal goodsDiscountAmount;

    /**
     * 现折金额
     */
    @ApiModelProperty(value = "现折金额")
    private BigDecimal cashDiscountAmount;

    /**
     * 开票金额
     */
    @ApiModelProperty(value = "开票金额")
    private BigDecimal remainInvoiceAllAmount;

}

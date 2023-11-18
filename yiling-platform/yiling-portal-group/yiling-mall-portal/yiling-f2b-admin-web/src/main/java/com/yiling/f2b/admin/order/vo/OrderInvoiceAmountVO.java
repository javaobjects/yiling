package com.yiling.f2b.admin.order.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 开票金额
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderInvoiceAmountVO extends BaseVO {

    /**
     * 发票总额
     */

    @ApiModelProperty(value = "发票金额")
    private BigDecimal invoiceAllAmount;

    @ApiModelProperty(value = "货款金额")
    private BigDecimal goodsAmount;

    @ApiModelProperty(value = "订单折扣")
    private BigDecimal cashDiscountAmount;

    @ApiModelProperty(value = "订单折扣")
    private BigDecimal ticketDiscountAmount;
}

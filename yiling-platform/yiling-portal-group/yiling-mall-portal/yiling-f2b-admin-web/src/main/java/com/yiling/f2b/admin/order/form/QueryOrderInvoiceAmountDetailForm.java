package com.yiling.f2b.admin.order.form;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryOrderInvoiceAmountDetailForm {

    @NotNull
    @ApiModelProperty(value = "明细Id")
    private Long detailId;

    @NotNull
    @ApiModelProperty(value = "折扣金额")
    private BigDecimal goodsDiscountAmount;

    /**
     * 现折金额
     */

    @ApiModelProperty(value = "现折金额")
    private BigDecimal cashDiscountAmount = BigDecimal.ZERO;

    /**
     * 票折金额
     */

    @ApiModelProperty(value = "票折金额")
    private BigDecimal ticketDiscountAmount = BigDecimal.ZERO;


    @NotEmpty(message = "不能为空")
    @ApiModelProperty(value = "批次信息")
    private List<@Valid SaveOrderTicketBatchForm> saveOrderTicketBatchList;
}

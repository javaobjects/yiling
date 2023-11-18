package com.yiling.sales.assistant.app.invoice.form;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:wei.wang
 * @date:2021/7/2
 */
@Data
public class OrderDetailTicketDiscountForm implements java.io.Serializable {

    @NotNull
    @ApiModelProperty(value = "明细Id")
    private Long detailId;

    @ApiModelProperty(value = "票折方式：1-按比率 2-按金额")
    private Integer invoiceDiscountType;

    @ApiModelProperty(value = "票折折扣比率")
    private BigDecimal discountRate;

    @ApiModelProperty(value = "票折折扣金额")
    private BigDecimal totalDiscountAmount;

}

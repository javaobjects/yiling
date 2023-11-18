package com.yiling.f2b.admin.order.form;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
    private BigDecimal ticketDiscountRate;

    @ApiModelProperty(value = "票折折扣金额")
    private BigDecimal ticketDiscountAmount;


    /**
     * 出库单号
     */
    @NotBlank
    @ApiModelProperty(value = "出库单号")
    private String erpDeliveryNo;

    @NotEmpty(message = "不能为空")
    @ApiModelProperty(value = "批次信息")
    private List<@Valid SaveOrderTicketBatchForm> saveOrderTicketBatchList;

}

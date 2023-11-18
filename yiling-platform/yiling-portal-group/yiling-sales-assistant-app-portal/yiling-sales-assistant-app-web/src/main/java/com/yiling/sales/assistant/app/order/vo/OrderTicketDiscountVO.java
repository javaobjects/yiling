package com.yiling.sales.assistant.app.order.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 票折信息
 * @author:wei.wang
 * @date:2021/8/6
 */
@Data
public class OrderTicketDiscountVO {

    @ApiModelProperty(value = "票折单号")
    private String ticketDiscountNo;

    @ApiModelProperty(value = "票折金额")
    private BigDecimal ticketDiscountAmount;
}

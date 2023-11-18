package com.yiling.sales.assistant.app.payment.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 收营台页面 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/28
 */
@Data
@ApiModel("POP收银台集合")
public class ReceiptDeskPageVO  implements Serializable {

    @ApiModelProperty("订单数量")
    private Integer orderNum;

    @ApiModelProperty("订单总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("已选订单金额")
    private BigDecimal selectedAmount;

    @ApiModelProperty("折扣金额")
    private BigDecimal discountAmount;

    @ApiModelProperty("应付金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty("订单列表")
    private List<OrderListItemVO> orderList;


}

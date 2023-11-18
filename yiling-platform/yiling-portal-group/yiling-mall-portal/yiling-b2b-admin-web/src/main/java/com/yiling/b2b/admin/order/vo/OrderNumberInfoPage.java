package com.yiling.b2b.admin.order.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * B2B商家后台数量和订单信息
 */

@Data
public class OrderNumberInfoPage  {

    @ApiModelProperty(value = "订单信息")
    private Page<OrderPageVO> orderPage;

    @ApiModelProperty(value = "待发货数量")
    private Long unDeliverQuantity;

    @ApiModelProperty(value = "发货数量")
    private Long receiveQuantity;

    @ApiModelProperty(value = "账期代付款")
    private Long paymentDayQuantity;
}

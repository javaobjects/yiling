package com.yiling.f2b.web.order.form;


import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
*
*@author:tingwei.chen
*@date:2021/6/28
*/

@Data
@Accessors(chain = true)
public class OrderDetailForm {

    @ApiModelProperty(value = "订单详情id", required = true)
    private String detailId;
    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id", required = true)
    private String goodsId;
    /**
     * 商品批次集合
     */
    @ApiModelProperty(value = "商品批次集合", required = true)
    private List<OrderDeliveryForm> orderDeliveryList;
}

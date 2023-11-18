package com.yiling.hmc.admin.order.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/3/25
 */
@Data
public class OrderPageVO extends OrderVO {

    @ApiModelProperty("订单明细信息")
    private List<OrderDetailVO> orderDetailList;

    @ApiModelProperty("收货人信息")
    private OrderRelateUserVO orderRelateUser;
}

package com.yiling.f2b.web.order.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/** 选择支付方式计算折扣
 * @author zhigang.guo
 * @date: 2022/11/7
 */
@Data
@AllArgsConstructor
public class OrderDiscountVO implements java.io.Serializable {

    @ApiModelProperty(value = "配送商Eid")
    private Long distributorEid;

    @ApiModelProperty(value = "订单商品总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "现折优惠金额")
    private BigDecimal cashDiscountAmount;

    @ApiModelProperty(value = "订单应付金额")
    private BigDecimal paymentAmount;


}

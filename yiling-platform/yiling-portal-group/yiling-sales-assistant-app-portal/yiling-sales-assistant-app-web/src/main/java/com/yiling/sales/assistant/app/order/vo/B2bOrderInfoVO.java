package com.yiling.sales.assistant.app.order.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.app.order.vo
 * @date: 2022/1/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class B2bOrderInfoVO extends OrderFullDetailInfoVO{

    @ApiModelProperty(value = "收货地址信息")
    private OrderAddressVO orderAddress;

    /**
     * 平台优惠劵金额
     */
    @ApiModelProperty(value = "平台优惠劵金额")
    private BigDecimal platformCouponDiscountAmount;

    /**
     * 商家优惠券金额
     */
    @ApiModelProperty(value = "商家优惠券金额")
    private BigDecimal couponDiscountAmount;
}

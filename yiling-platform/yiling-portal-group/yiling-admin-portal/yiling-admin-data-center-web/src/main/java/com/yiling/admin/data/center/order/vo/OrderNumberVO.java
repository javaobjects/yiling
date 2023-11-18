package com.yiling.admin.data.center.order.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单数量统计VO
 *
 * @author:wei.wang
 * @date:2021/6/18
 */
@Data
public class OrderNumberVO implements java.io.Serializable {
    /**
     * 今天订单数量
     */
    @ApiModelProperty(value = "今天订单数量")
    private Integer todayOrderNum;

    /**
     * 昨天订单数量
     */
    @ApiModelProperty(value = "昨天订单数量")
    private Integer yesterdayOrderNum;

    /**
     * 进一年数据
     */
    @ApiModelProperty(value = "进一年数据")
    private Integer yearOrderNum;

    /**
     * 货款金额
     */
    @ApiModelProperty(value = "货款金额")
    private BigDecimal totalAmount;

    /**
     * 优惠总金额
     */
    @ApiModelProperty(value = "优惠总金额")
    private BigDecimal discountAmount;

    /**
     * 支付总金额
     */
    @ApiModelProperty(value = "支付总金额")
    private BigDecimal paymentAmount;

}


package com.yiling.f2b.admin.order.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 使用票折订单信息
 * @author:wei.wang
 * @date:2021/7/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderMakeTicketInfoVO extends BaseVO {
    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 订单金额
     */
    @ApiModelProperty(value = "货款金额金额")
    private BigDecimal totalAmount;

    /**
     * 现折金额
     */
    @ApiModelProperty(value = "现折金额")
    private BigDecimal cashDiscountAmount;

    /**
     * 使用票折金额
     */
    @ApiModelProperty(value = "使用票折金额")
    private BigDecimal useAmount;

    /**
     * 支付金额
     */
    @ApiModelProperty(value = "支付金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty(value = "申请时间")
    private Date createTime;
}


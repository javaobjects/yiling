package com.yiling.b2b.admin.paymentdays.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 账期到期提醒金额总计 VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/11/1
 */
@Data
public class PaymentDaysExpireOrderCountVO{

    /**
     * 订单金额（支付的时候是多少钱，那订单金额就是多少钱）
     */
    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    /**
     * 退款金额
     */
    @ApiModelProperty("退款金额")
    private BigDecimal returnAmount;

    /**
     * 已还款金额
     */
    @ApiModelProperty("已还款金额")
    private BigDecimal repaymentAmount;

    /**
     * 待还款金额
     */
    @ApiModelProperty("待还款金额")
    private BigDecimal unRepaymentAmount;

}

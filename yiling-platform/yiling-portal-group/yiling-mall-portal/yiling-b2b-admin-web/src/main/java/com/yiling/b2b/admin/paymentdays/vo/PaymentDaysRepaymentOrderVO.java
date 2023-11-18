package com.yiling.b2b.admin.paymentdays.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 账期已还款订单 VO
 * @author lun.yu
 * @date 2021/10/29
 */
@Data
@Accessors(chain = true)
public class PaymentDaysRepaymentOrderVO extends BaseVO {

    /**
     * orderNo
     */
    @ApiModelProperty("orderNo")
    private String orderNo;

    /**
     * 下单时间
     */
    @ApiModelProperty("下单时间")
    private Date createTime;

    /**
     * 支付的时候是多少钱，那订单金额就是多少钱
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
     * 还款状态：1-未还款 2-部分还款 3-全部还款
     */
    @ApiModelProperty("还款状态：1-未还款 2-部分还款 3-全部还款")
    private Integer repaymentStatus;

    /**
     * 还款时间
     */
    @ApiModelProperty("还款时间")
    private Date repaymentTime;

}

package com.yiling.b2b.admin.paymentdays.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 账期到期提醒订单列表 VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/29
 */
@Data
@Accessors(chain = true)
public class PaymentDaysOrderVO extends BaseVO {

    @ApiModelProperty(value = "订单ID",hidden = true)
    private Long orderId;

    /**
     * 订单号
     */
    @ApiModelProperty("订单号")
    private String orderNo;
    /**
     * 采购商名称
     */
    @ApiModelProperty("采购商名称")
    private String customerName;

    /**
     * 采购商id
     */
    @ApiModelProperty("采购商id")
    private Long customerEid;

    /**
     * 账期（天）
     */
    @ApiModelProperty("账期")
    private Integer period;

    /**
     * 使用金额
     */
    @ApiModelProperty(value = "使用金额",hidden = true)
    private BigDecimal usedAmount;

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
     * 应还款日期
     */
    @ApiModelProperty("应还款日期")
    private Date expirationTime;

    /**
     * 发货时间
     */
    @ApiModelProperty("发货时间")
    private Date deliveryTime;

    /**
     * 还款状态：1-未还款 2-部分还款 3-全部还款
     */
    @ApiModelProperty("还款状态：1-未还款 2-部分还款 3-全部还款")
    private Integer repaymentStatus;

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

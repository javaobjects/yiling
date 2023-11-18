package com.yiling.b2b.admin.paymentdays.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * B2B-账期订单金额总计 VO
 * @author lun.yu
 * @date 2021/10/29
 */
@ApiModel
@Data
@Accessors(chain = true)
public class PaymentDaysOrderAmountCountVO extends BaseVO {

    /**
     * 账户类型：1-以岭账期 2-非以岭账期 3-工业直属账期
     */
    @ApiModelProperty("账户类型：1-以岭账期 2-非以岭账期 3-工业直属账期")
    private Integer type;

    /**
     * 授信主体
     */
    @ApiModelProperty(value = "授信主体(供应商)")
    private String ename;

    /**
     * 客户名称（采购商）
     */
    @ApiModelProperty(value = "客户名称（采购商）")
    private String customerName;

    /**
     * 已使用金额 = 订单金额 - 退款金额
     */
    @ApiModelProperty("已使用额度")
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

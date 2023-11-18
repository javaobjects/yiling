package com.yiling.user.payment.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 账期订单金额总计 DTO
 * @author lun.yu
 * @date 2021/10/29
 */
@Data
@Accessors(chain = true)
public class PaymentDaysOrderAmountCountDTO extends BaseDTO {

    /**
     * 账户类型：1-以岭账期 2-非以岭账期 3-工业直属账期
     */
    private Integer type;

    /**
     * 授信主体
     */
    private String ename;

    /**
     * 客户名称（采购商）
     */
    private String customerName;

    /**
     * 已使用金额 = 订单金额 - 退款金额
     */
    private BigDecimal usedAmount;

    /**
     * 订单金额（支付的时候是多少钱，那订单金额就是多少钱）
     */
    private BigDecimal orderAmount;

    /**
     * 退款金额
     */
    private BigDecimal returnAmount;

    /**
     * 已还款金额
     */
    private BigDecimal repaymentAmount;

    /**
     * 待还款金额
     */
    private BigDecimal unRepaymentAmount;

}

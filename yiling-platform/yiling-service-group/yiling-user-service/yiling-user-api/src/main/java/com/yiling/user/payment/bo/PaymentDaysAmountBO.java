package com.yiling.user.payment.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 客户账期金额 BO
 *
 * @author lun.yu
 * @date 2021年8月14日
 */
@Data
@Accessors(chain = true)
public class PaymentDaysAmountBO implements Serializable {

    /**
     * 使用总金额
     */
    private BigDecimal totalUsedAmount;

    /**
     * 总退款金额
     */
    private BigDecimal totalReturnAmount;

    /**
     * 支付总金额
     */
    private BigDecimal totalPayAmount;

}

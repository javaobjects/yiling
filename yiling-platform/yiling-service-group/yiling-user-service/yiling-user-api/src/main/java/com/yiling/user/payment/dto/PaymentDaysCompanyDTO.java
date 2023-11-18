package com.yiling.user.payment.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 集团账期总额度
 * </p>
 *
 * @author tingwei.chen
 * @date 2021-07-02
 */
@Data
@Accessors(chain = true)
public class PaymentDaysCompanyDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;


    /**
     * 账期总额度
     */
    private BigDecimal totalAmount;

    /**
     * 已使用额度
     */
    private BigDecimal usedAmount;

    /**
     * 已还款额度
     */
    private BigDecimal repaymentAmount;


}

package com.yiling.user.payment.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("payment_days_company")
public class PaymentDaysCompanyDO extends BaseDO {

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

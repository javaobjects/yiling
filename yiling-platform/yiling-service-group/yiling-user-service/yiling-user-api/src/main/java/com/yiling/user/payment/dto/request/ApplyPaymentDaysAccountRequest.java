package com.yiling.user.payment.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 账户临时额度 Request
 *
 * @author: tingwei.chen
 * @date: 2021/7/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ApplyPaymentDaysAccountRequest extends BaseDO {
    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 临时额度
     */
    private BigDecimal temporaryAmount;
    /**
     * 审核
     */
    private int status;
}

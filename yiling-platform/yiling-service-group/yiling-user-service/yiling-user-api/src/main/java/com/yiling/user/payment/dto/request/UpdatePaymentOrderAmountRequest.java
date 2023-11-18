package com.yiling.user.payment.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 更新账期订单的还款金额 request
 * @author: lun.yu
 * @date: 2021/8/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdatePaymentOrderAmountRequest extends BaseRequest {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 还款金额
     */
    private BigDecimal repaymentAmount;

}

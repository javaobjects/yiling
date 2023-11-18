package com.yiling.order.order.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/12/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RefundOrderTicketDiscountRequest extends BaseRequest {
    /**
     * 订单票折记录ID
     */
    private Long       id;

    /**
     * 退还额度
     */
    private BigDecimal refundAmount;
}

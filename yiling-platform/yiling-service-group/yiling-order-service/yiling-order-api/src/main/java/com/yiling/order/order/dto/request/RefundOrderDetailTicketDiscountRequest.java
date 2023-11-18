package com.yiling.order.order.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单明细票折信息 退还票折
 * 
 * @author: yong.zhang
 * @date: 2021/12/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RefundOrderDetailTicketDiscountRequest extends BaseRequest {
    /**
     * 订单明细票折信息ID
     */
    private Long       id;

    /**
     * 退还额度
     */
    private BigDecimal refundAmount;
}

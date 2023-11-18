package com.yiling.order.order.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.dto
 * @date: 2021/9/30
 */
@Data
@Accessors(chain = true)
@Builder
public class ContacterOrderSumDTO implements Serializable {
    private static final long serialVersionUID=-8897818117155928378L;
    /**
     * 联系人ID
     */
    private Long contacterId;

    /**
     * 订单总金额
     */
    private BigDecimal totalOrderMoney;

    /**
     * 订单收货数量
     */
    private Integer totalReceiveTotalQuantity;

}

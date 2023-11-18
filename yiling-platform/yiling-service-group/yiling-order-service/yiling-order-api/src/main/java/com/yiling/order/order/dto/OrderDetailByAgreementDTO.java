package com.yiling.order.order.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询3个月签收订单明细使用余协议
 * @author:wei.wang
 * @date:2021/8/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderDetailByAgreementDTO extends OrderDetailDTO {

    /**
     * 发货金额
     */
    private BigDecimal deliveryAmount;

    /**
     * 发货数量
     */
    private Integer deliveryQuantity;
}

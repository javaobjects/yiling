package com.yiling.order.order.dto;

import java.math.BigDecimal;

import lombok.Data;



/**
 * @author:wei.wang
 * @date:2021/11/8
 */
@Data
public class OrderAssistantConfirmQuantityAmountDTO implements java.io.Serializable {

    /**
     * 总金额
     */
    BigDecimal totalAmount;

    /**
     * 总数量
     */
    Integer totalQuantity;
}

package com.yiling.order.order.dto;


import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * (大屏)订单发货报表数据统计
 */
@Data
public class OrderDeliveryReportCountDTO implements java.io.Serializable  {
    private static final long serialVersionUID = -7631547950651654406L;

    /**
     * 发货时间
     */
    private Date createTime;

    /**
     * 统计卖家EID
     */
    private Long buyerEid;

    /**
     * 发货数量
     */
    private Integer deliveryQuantity;

    /**
     * 发货金额
     */
    private BigDecimal deliveryAmount;

}

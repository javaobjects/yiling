package com.yiling.order.order.bo;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Data;

/**
 *  订单反审导致，退款金额，库存发生变动BO
 *
 * @author: zhigang.guo
 * @date: 2021/8/4
 */
@Data
public class OrderModifyAuditChangeBO implements java.io.Serializable {

    /**
     * 退款金额
     */
    private BigDecimal returnTotalAmount;

    /**
     * 是否重建退货单
     */
    private Boolean isProductReturnOrder;

    /**
     * 是否扣减月结账户
     */
    private Boolean isReducePaymentAccount;

    /**
     * 库存变化结果
     */
    private Map<Long,Integer> inventoryChangeMap;

    /**
     * 冻结库存
     */
    private Map<Long,Integer> frozenChangeMap;
}

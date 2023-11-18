package com.yiling.order.order.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单现折记录
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-07-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("order_cash_discount")
public class OrderCashDiscountDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 协议版本号
     */
    private String agreementVersion;

    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;


}

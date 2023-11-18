package com.yiling.order.settlement.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/8/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SettlementDetailDTO extends BaseDTO {

    /**
     * 结算单ID
     */
    private Long settlementId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单ID
     */
    private Long sellerEid;

    /**
     * 订单ID
     */
    private Long buyerEid;

    /**
     * 订单明细
     */
    private Long orderDetailId;

    /**
     * 以岭商品Id
     */
    private Long goodsId;

    /**
     * 回款核销金额
     */
    private BigDecimal backAmount;

    /**
     * 红票核销金额
     */
    private BigDecimal discountsAmount;
}

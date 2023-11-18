package com.yiling.order.settlement.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
public class SettlementFullDTO extends BaseDTO {

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

    private Date deliveryTime;

    /**
     * 回款核销金额
     */
    private BigDecimal backAmount;

    /**
     * 红票核销金额
     */
    private BigDecimal discountsAmount;

    private Integer backAmountType;

    /**
     * 还款时间
     */
    private Date backTime;

    private List<SettlementDetailDTO> settlementDetailDTOList;
}

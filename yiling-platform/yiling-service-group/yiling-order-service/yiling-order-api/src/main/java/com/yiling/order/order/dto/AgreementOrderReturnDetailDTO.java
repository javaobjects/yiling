package com.yiling.order.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/9/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementOrderReturnDetailDTO extends BaseDTO {
    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 退货单审核时间
     */
    private Date returnAuditTime;
    /**
     * 商品ID
     */
    private Long goodsId;
    /**
     * 订单明细ID
     */
    private Long detailId;
    /**
     * 退货商品小计
     */
    private BigDecimal returnAmount;
    /**
     * 退货数量
     */
    private Integer returnQuantity;
}

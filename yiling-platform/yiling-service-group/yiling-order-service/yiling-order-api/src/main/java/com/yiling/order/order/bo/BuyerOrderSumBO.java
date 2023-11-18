package com.yiling.order.order.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.bo
 * @date: 2021/9/30
 */
@Data
@Accessors(chain = true)
public class BuyerOrderSumBO implements Serializable {

    private static final long serialVersionUID=434828757698867300L;
    /**
     * 订单总金额
     */
    private BigDecimal totalOrderMoney;

    /**
     * 购买商品件数
     */
    private Long orderNumberType;

    /**
     * 收货总金额
     */
    private BigDecimal receiveTotalAmount;

    /**
     * 收货总数量
     */
    private Integer receiveTotalQuantity;

    /**
     * 发货总数量
     */
    private Integer deliveryTotalQuantity;

    /**
     * 发货总金额
     */
    private BigDecimal deliveryTotalAmount;

    /**
     * 买家订单明细统计(按照商品Id,规格统计)
     */
    private Page<BuyerOrderDetailSumBO> orderDetailSumBOPage;

}

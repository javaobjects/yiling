package com.yiling.order.order.bo;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class BuyerOrderDetailSumBO implements Serializable {

    private static final long serialVersionUID=-1331251855964649159L;

    /**
     * 商品标准库ID
     */
    private Long standardId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品规格
     */
    private String goodsSpecification;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 收货数量
     */
    private Integer receiveQuantity;

    /**
     * 收货金额
     */
    private BigDecimal receiveAmount;

    /**
     * 发货数量
     */
    private Integer deliveryQuantity;

    /**
     * 发货金额
     */
    private BigDecimal deliveryAmount;
}

package com.yiling.pricing.goods.bo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 统计商品价格区间
 * @author: yuecheng.chen
 * @date: 2021/6/23 0023
 */
@Data
public class CountGoodsPriceBO implements java.io.Serializable {

    private static final long serialVersionUID = -7344137972098697229L;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 最小商品价格
     */
    private BigDecimal minPrice;

    /**
     * 最大商品价格
     */
    private BigDecimal maxPrice;

    /**
     * 统计条目数
     */
    private Integer itemNum;
}

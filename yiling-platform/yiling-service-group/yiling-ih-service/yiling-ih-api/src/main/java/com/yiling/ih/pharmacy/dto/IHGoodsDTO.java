package com.yiling.ih.pharmacy.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 互联网医院商品信息
 */
@Data
@Accessors(chain = true)
public class IHGoodsDTO implements java.io.Serializable {

    /**
     * 销售价
     */
    private BigDecimal salePrice;

    /**
     * 成本价
     */
    private BigDecimal selfPrice;

    /**
     * 库存
     */
    private Long stock;

    /**
     * 安全库存
     */
    private Long safeStock;

}

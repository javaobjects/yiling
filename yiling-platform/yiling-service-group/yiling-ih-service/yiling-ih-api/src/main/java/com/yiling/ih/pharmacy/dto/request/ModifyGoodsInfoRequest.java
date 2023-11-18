package com.yiling.ih.pharmacy.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * 修改销售价格库存信息 Request
 *
 * @author: fan.shen
 * @date: 2023/5/6
 */
@Data
@Accessors(chain = true)
public class ModifyGoodsInfoRequest implements java.io.Serializable {

    private Long id;

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

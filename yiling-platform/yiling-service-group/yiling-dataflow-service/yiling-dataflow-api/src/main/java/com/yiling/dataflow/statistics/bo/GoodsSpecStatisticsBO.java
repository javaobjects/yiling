package com.yiling.dataflow.statistics.bo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/7/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsSpecStatisticsBO extends BaseDTO {

    /**
     * 商品+规格id
     */
    private Long specificationId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 规格名称
     */
    private String spec;

    /**
     * 采购数量
     */
    private BigDecimal poQuantity;

    /**
     * 销售数量
     */
    private BigDecimal soQuantity;

    /**
     * 库存数量
     */
    private BigDecimal gbQuantity;
}

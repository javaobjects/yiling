package com.yiling.dataflow.statistics.dto;

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
public class GoodsSpecStatisticsDTO extends BaseDTO {

    private Long specificationId;

    private String goodsName;

    private String spec;

    private BigDecimal poQuantity;

    private BigDecimal soQuantity;

    private BigDecimal beginMonthQuantity;

    private BigDecimal endMonthQuantity;
}

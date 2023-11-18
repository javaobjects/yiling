package com.yiling.dataflow.statistics.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 库存预警对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StockForecastInfoDTO extends BaseDTO {
    private Integer status;
    private String referenceAbleSaleDays1;
    private String referenceAbleSaleDays2;
    //90天的日均销量
    private String referenceAbleSaleDays90;
    //近5天在途订单出库数量
    private Long deliveryQuantity;
    private Long replenishDays;
    private Long replenishQuantityMax;
    private Long replenishQuantityMin;
    private StockReferenceTimeInfoDTO referenceTimeInfoVo1;
    private StockReferenceTimeInfoDTO referenceTimeInfoVo2;
    private List<StockForecastIconDTO> stockForecastIconVoList;

}

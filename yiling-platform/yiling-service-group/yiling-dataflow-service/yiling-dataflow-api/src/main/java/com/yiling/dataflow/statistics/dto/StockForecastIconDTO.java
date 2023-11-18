package com.yiling.dataflow.statistics.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 库存预警库存图标对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StockForecastIconDTO extends BaseDTO {
    private Date pointTime;
    private Long specificationId;
    private Long replenish1;
    private Long replenish2;
    private Long stockForecastQuantity1;
    private Long stockForecastQuantity2;
    private Long nearStockQuantity;
}

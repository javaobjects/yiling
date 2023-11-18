package com.yiling.dataflow.statistics.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 库存预警图表时间轴数据
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StockForecastIconDataDTO extends BaseDTO {
    private String pointDate;
    private Long pointValue;
}

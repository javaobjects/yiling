package com.yiling.dataflow.statistics.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 库存预警库存图标对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StockForecastSaleIconDTO extends BaseDTO {
    private Date pointTime;
    private Long redValue;
    private Long blueValue;
}

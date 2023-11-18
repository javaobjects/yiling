package com.yiling.dataflow.statistics.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 库存预警库存图标对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StockSaleIconDTO extends BaseDTO {
    private Long referenceAverageDailySale1;
    private Long referenceAverageDailySale;
    private List<StockSaleIconDataDTO> near90DailySaleList;
}

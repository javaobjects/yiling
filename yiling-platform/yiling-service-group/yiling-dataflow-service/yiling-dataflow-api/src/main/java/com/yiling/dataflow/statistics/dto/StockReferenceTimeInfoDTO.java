package com.yiling.dataflow.statistics.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 补货时间段内对象信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StockReferenceTimeInfoDTO extends BaseDTO {
    /**
     * 参考时间
     */
    private Long referenceTime;
    /**
     * 日均销量
     */
   private Long averageDailySales;
    /**
     * 补货天数
     */
    private Long replenishDays;
    /**
     * 当前库存数量
     */
    private Long curStockQuantity;
    /**
     * 建议安全库存
     */
    private Long safeStockQuantity;
    /**
     * 建议补货量
     */
    private Long replenishStockQuantity;

}

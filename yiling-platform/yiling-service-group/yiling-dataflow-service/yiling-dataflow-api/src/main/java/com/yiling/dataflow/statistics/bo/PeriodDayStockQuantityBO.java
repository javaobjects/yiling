package com.yiling.dataflow.statistics.bo;

import lombok.Data;

@Data
public class PeriodDayStockQuantityBO {
   private String dateTime;
    /**
     * 商品规格ID
     */
    private Long specificationId;

    /**
     * 库存数量
     */
    private Long stockQuantity;
}

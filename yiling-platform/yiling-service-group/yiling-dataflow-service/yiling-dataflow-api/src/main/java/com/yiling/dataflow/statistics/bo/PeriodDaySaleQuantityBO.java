package com.yiling.dataflow.statistics.bo;

import lombok.Data;

@Data
public class PeriodDaySaleQuantityBO {
   private String dateTime;
    /**
     * 商品规格ID
     */
    private Long specificationId;

    /**
     * 销售数量
     */
    private Long soQuantity;
}

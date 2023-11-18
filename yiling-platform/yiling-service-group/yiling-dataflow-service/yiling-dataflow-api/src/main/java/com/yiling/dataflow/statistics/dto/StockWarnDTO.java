package com.yiling.dataflow.statistics.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 库存预警
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StockWarnDTO extends BaseDTO {
   // private Long eid;
    private Long crmEnterpriseId;
    private Integer day;
    private String ename;
    private String enameLevel;
    private String specificationId;
    private String goodsName;
    private Long saleAvg;// 日均销量
    private BigDecimal supportDay;//平均天数
    private Long supplementQuantity;
    private Long stockQuantity;//库存数量
    private BigDecimal near3AbleSaleDays;
    private BigDecimal near30AbleSaleDays;
    private Long near3AverageDailySales;
    private Long near30AverageDailySales;
    private Integer status;
    private Long replenishQuantityMax;
    private Long replenishQuantityMin;

}

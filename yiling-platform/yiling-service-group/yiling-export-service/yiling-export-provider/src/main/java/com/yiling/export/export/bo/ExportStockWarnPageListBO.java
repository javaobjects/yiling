package com.yiling.export.export.bo;

import lombok.Data;
/**
 * 库存预警分页列表导出BO
 */
import java.math.BigDecimal;
@Data
public class ExportStockWarnPageListBO {
    //private Long eid;
    private Long crmEnterpriseId;
    private Integer day;
    private String ename;
    private String enameLevel;
    private String specificationId;
    private String goodsName;
    private Long saleAvg;// 日均销量
    private BigDecimal supportDay;//平均天数
    private String supportDayStr;//平均天数
    private Long supplementQuantity;
    private Long stockQuantity;//库存数量
    private BigDecimal near3AbleSaleDays;
    private String near3AbleSaleDaysStr;
    private BigDecimal near30AbleSaleDays;
    private String near30AbleSaleDaysStr;
    private Long near3AverageDailySales;
    private Long near30AverageDailySales;
    private Integer status;
    private String statusStr;
    private Long replenishQuantityMax;
    private Long replenishQuantityMin;
    private String enameAndGoodsName;
}

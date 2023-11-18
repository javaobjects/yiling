package com.yiling.sjms.goodsplans.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 库存预警
 */
@Data
public class StockWarnVo {
    @ApiModelProperty(value = "经销商id")
    private String eid;
    @ApiModelProperty(value = "机构ID")
    private String crmEnterpriseId;
    @ApiModelProperty(value = "经销商")
    private String ename;
    @ApiModelProperty(value = "渠道等级")
    private String enameLevel;
    @ApiModelProperty(value = "商品")
    private String goodsName;
    @ApiModelProperty(value = "商品规格ID")
    private String specificationId;
    @ApiModelProperty(value = "近3天可销售天数")
    private BigDecimal near3AbleSaleDays;
    @ApiModelProperty(value = "近30天的可销售天数")
    private BigDecimal near30AbleSaleDays;
    @ApiModelProperty(value = "近3天的日均销量")
    private Long near3AverageDailySales;
    @ApiModelProperty(value = "近30天的日均销量")
    private Long near30AverageDailySales;
    @ApiModelProperty(value = "库存数量")
    private Long stockQuantity;
    @ApiModelProperty("库存状态")
    private Integer status;
    @ApiModelProperty(value = "补货建议补货量上限")
    private Long replenishQuantityMax;
    @ApiModelProperty(value = "补货建议补货量下限")
    private Long replenishQuantityMin;
}

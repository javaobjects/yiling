package com.yiling.sjms.goodsplans.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 库存预警图标部分
 */
@Data
public class StockWarnIconVo {
    @ApiModelProperty(value = "经销商id")
    private Long eid;
    @ApiModelProperty(value = "经销商")
    private String ename;
    @ApiModelProperty(value = "渠道等级")
    private String enameLevel;
    @ApiModelProperty(value = "商品")
    private String goodsName;
    @ApiModelProperty(value = "商品规格ID")
    private String specificationId;//商品规则ID
    @ApiModelProperty(value = "日均销量")
    private Long saleAvg;// 日均销量
    @ApiModelProperty(value = "可销天数")
    private BigDecimal supportDay;//平均天数
    @ApiModelProperty(value = "库存数量")
    private Long stockQuantity;//库存数量


}

package com.yiling.sjms.goodsplans.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 库存预警对象
 */
@Data
public class StockForecastInfoVo {
    @ApiModelProperty("库存状态")
    private Integer status;
    @ApiModelProperty("参数时间1可销售天数")
    private String referenceAbleSaleDays1;
    @ApiModelProperty("参数时间2可销售天数")
    private String referenceAbleSaleDays2;
    //90天的日均销量
    @ApiModelProperty("90天的日均销量")
    private String referenceAbleSaleDays90;

    @ApiModelProperty(value = "补货天数")
    private Long replenishDays;
    @ApiModelProperty(value = "近5天在途订单出库数量")
    private Long deliveryQuantity;
    @ApiModelProperty(value = "补货建议补货量上限")
    private Long replenishQuantityMax;
    @ApiModelProperty(value = "补货建议补货量下限")
    private Long replenishQuantityMin;
    @ApiModelProperty(value = "参考时间1的计算结果")
    private StockReferenceTimeInfoVo referenceTimeInfoVo1;
    @ApiModelProperty(value = "参考时间2的计算结果")
    private StockReferenceTimeInfoVo referenceTimeInfoVo2;
    @ApiModelProperty(value = "图标数据")
    private List<StockForecastIconVo> stockForecastIconVoList;


}

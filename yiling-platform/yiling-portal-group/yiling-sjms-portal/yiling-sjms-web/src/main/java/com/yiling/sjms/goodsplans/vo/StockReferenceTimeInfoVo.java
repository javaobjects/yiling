package com.yiling.sjms.goodsplans.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 补货时间段内对象信息
 */
@Data
public class StockReferenceTimeInfoVo {
    @ApiModelProperty(value = "参考时间")
    private Long referenceTime;
    @ApiModelProperty(value = "日均销量")
   private Long averageDailySales;
    @ApiModelProperty(value = "补货天数")
    private Long replenishDays;
    @ApiModelProperty(value = "当前库存数量")
    private Long curStockQuantity;
    @ApiModelProperty(value = "建议安全库存")
    private Long safeStockQuantity;
    @ApiModelProperty(value = "建议补货量")
    private Long replenishStockQuantity;

}

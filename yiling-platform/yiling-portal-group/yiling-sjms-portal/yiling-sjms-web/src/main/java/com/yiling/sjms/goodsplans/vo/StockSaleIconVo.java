package com.yiling.sjms.goodsplans.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 库存预警库存图标对象
 */
@Data
public class StockSaleIconVo {
    @ApiModelProperty(value = "参数时间1日均销量")
    private Long referenceAverageDailySale1;
    @ApiModelProperty(value = "参考时间2日均销量")
    private Long referenceAverageDailySale;
    @ApiModelProperty(value = "预测库存1")
    private List<StockSaleIconDataVo> near90DailySaleList;
}

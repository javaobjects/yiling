package com.yiling.sjms.goodsplans.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 库存预警库存图标对象
 */
@Data
public class StockForecastIconVo {
    @ApiModelProperty(value = "时间点")
    private Date pointTime;
    @ApiModelProperty(value = "补货建议1数据")
    private Long replenish1;
    @ApiModelProperty(value = "补货建议2数据")
    private Long replenish2;
    @ApiModelProperty(value = "预测库存1")
    private Long stockForecastQuantity1;
    @ApiModelProperty(value = "预测库存2")
    private Long stockForecastQuantity2;
    @ApiModelProperty(value = "近期库存数量")
    private Long nearStockQuantity;
}

package com.yiling.sjms.goodsplans.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 库存预警图表时间轴数据
 */
@Data
public class StockForecastIconDataVo {
    @ApiModelProperty(value = "时间点")
    private String pointDate;
    @ApiModelProperty(value = "时间值")
    private Long pointValue;
}

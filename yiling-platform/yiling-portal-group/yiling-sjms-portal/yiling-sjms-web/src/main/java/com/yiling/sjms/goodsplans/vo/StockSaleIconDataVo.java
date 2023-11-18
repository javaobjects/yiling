package com.yiling.sjms.goodsplans.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 库存预警-销售图表时间轴数据
 */
@Data
public class StockSaleIconDataVo {
    @ApiModelProperty(value = "时间点")
    private String pointDate;
    @ApiModelProperty(value = "时间值")
    private Long pointValue;
}

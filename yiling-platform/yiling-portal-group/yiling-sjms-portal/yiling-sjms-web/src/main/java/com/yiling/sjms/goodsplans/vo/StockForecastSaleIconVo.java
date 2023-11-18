package com.yiling.sjms.goodsplans.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 库存预警库存图标对象
 */
@Data
public class StockForecastSaleIconVo {
    @ApiModelProperty(value = "时间点")
    private Date pointTime;
    @ApiModelProperty(value = "红色线点固定")
    private Long redValue;
    @ApiModelProperty(value = "蓝色线点位")
    private Long blueValue;
}

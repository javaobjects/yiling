package com.yiling.sjms.goodsplans.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FlowAnalyseGoodsVo {
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品规格ID")
    private String specificationId;
}
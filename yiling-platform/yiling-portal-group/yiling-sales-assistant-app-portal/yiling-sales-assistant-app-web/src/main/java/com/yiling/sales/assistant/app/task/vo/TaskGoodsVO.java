package com.yiling.sales.assistant.app.task.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * app端任务详情页-商品
 * @author ray
 */
@Data
@Accessors(chain = true)
public class TaskGoodsVO {

    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;
    @ApiModelProperty(value = "佣金")
    private BigDecimal commission;

    private String name;

    private String specifications;
    @ApiModelProperty(value = "规格")
    private String sellSpecifications;
    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    private Boolean canSplit;


    private Integer middlePackage;
    @ApiModelProperty(value = "商品图片")
    private String goodsPic;


}

package com.yiling.admin.sales.assistant.task.form;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/9/14
 */
@Data
@Accessors(chain = true)
public class UpdateTaskGoodsRelationForm {
    @ApiModelProperty(value = "任务商品主键")
    private Long taskGoodsId;

    @ApiModelProperty(value = "任务商品销售价（追加新商品传）")
    private BigDecimal salePrice;

    @ApiModelProperty(value = "佣金 企业任务不设")
    private BigDecimal commission;

    @ApiModelProperty(value = "商品主键（追加新商品传）")
    private Long goodsId;

    @ApiModelProperty(value = "商品名称（追加新商品传）")
    private String goodsName;

    @ApiModelProperty(value = "出货价（追加新商品传）")
    private BigDecimal outPrice;

    /**
     * 商销价
     */
    @ApiModelProperty(value = "商销价（追加新商品传）")
    private BigDecimal  sellPrice;
}
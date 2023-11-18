package com.yiling.sales.assistant.app.enterprise.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品信息 VO
 *
 * @author: lun.yu
 * @date: 2021/9/26
 */
@Data
public class ProductItemVO {

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品规格")
    private String specifications;

    @ApiModelProperty("购买数量")
    private Integer goodsQuantity;

    @ApiModelProperty("商品单价")
    private BigDecimal goodsPrice;

    @ApiModelProperty("商品总价")
    private BigDecimal totalPrice;

}

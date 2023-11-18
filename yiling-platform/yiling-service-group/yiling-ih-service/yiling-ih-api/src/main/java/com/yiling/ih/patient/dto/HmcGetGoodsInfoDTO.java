package com.yiling.ih.patient.dto;


import lombok.Data;

import java.math.BigDecimal;


/**
 * 获取IH商品信息
 *
 * @author: fan.shen
 * @data: 2023/05/09
 */
@Data
public class HmcGetGoodsInfoDTO implements java.io.Serializable {

    private static final long serialVersionUID = 3768586786173659462L;

    // @ApiModelProperty(value = "配送商商品id")
    private Integer id;

    // @ApiModelProperty(value = "销售价")
    private String salePrice;

    // @ApiModelProperty(value = "成本价")
    private String marketPrice;

    // @ApiModelProperty(value = "库存")
    private BigDecimal stock;

    // @ApiModelProperty(value = "安全库存")
    private Integer safeStock;
}

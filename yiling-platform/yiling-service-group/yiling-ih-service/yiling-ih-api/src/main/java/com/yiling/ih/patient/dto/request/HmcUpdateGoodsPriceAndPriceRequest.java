package com.yiling.ih.patient.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 修改商品价格库存信息 request
 *
 * @author fan.shen
 * @date 2023-05-18
 */
@Data
public class HmcUpdateGoodsPriceAndPriceRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

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

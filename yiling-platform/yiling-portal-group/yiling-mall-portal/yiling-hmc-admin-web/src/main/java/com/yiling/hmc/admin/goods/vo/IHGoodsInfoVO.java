package com.yiling.hmc.admin.goods.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * IH商品信息VO
 *
 * @author: fan.shen
 * @data: 2023/05/09
 */
@Data
public class IHGoodsInfoVO {

    private static final long serialVersionUID = 3768586786173659462L;

    private Integer id;

    @ApiModelProperty(value = "销售价")
    private String salePrice;

    @ApiModelProperty(value = "成本价")
    private String marketPrice;

    @ApiModelProperty(value = "库存")
    private BigDecimal stock;

    @ApiModelProperty(value = "安全库存")
    private Integer safeStock;
}

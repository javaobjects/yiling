package com.yiling.common.web.goods.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品价格 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/15
 */
@Data
public class GoodsPriceVO {

    /**
     * 购买价
     */
    @ApiModelProperty("购买价")
    private BigDecimal buyPrice;

    @ApiModelProperty("最低价")
    private BigDecimal minPrice;

    @ApiModelProperty("最高价")
    private BigDecimal maxPrice;

    /**
     * 划线价
     */
    @ApiModelProperty("划线价")
    private BigDecimal linePrice;

    /**
     * 是否展示
     */
    @ApiModelProperty("是否展示价格 true-展示 false-不展示")
    private Boolean isShow;

}

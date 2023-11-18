package com.yiling.sales.assistant.app.cart.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 购物车商品数量 VO
 *
 * @author: zhigang.guo
 * @date: 2021/09/15
 */
@Data
@AllArgsConstructor
public class CartGoodsNumVO {

    /**
     * 购物车商品数量
     */
    @ApiModelProperty("购物车商品数量")
    private Integer cartGoodsNum;

}

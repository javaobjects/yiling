package com.yiling.f2b.web.cart.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 购物车商品数量 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/18
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

package com.yiling.f2b.web.cart.vo;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 购物车列表项 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/21
 */
@Data
public class CartDistributorVO {

    @ApiModelProperty("配送商企业ID")
    private Long distributorEid;

    @ApiModelProperty("配送商名称")
    private String distributorName;

    @ApiModelProperty("商品列表")
    private List<CartGoodsVO> cartGoodsList;

    @ApiModelProperty("已选择商品数")
    private Long selectedGoodsNum;

    @ApiModelProperty("已选择商品总金额")
    private BigDecimal selectedGoodsAmount;

    @ApiModelProperty("是否能勾选")
    private Boolean selectEnabled;

    @ApiModelProperty("是否选中")
    private Boolean selected;
}

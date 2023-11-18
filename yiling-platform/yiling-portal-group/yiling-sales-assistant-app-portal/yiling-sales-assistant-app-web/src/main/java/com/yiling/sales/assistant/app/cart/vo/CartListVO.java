package com.yiling.sales.assistant.app.cart.vo;

import java.math.BigDecimal;
import java.util.List;

import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 购物车列表 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/21
 */
@Data
public class CartListVO {

    @ApiModelProperty("购物车列表")
    private List<CartDistributorVO> cartDistributorList;

    @ApiModelProperty("已选择商品数")
    private Long selectedGoodsNum;

    @ApiModelProperty("已选择商品总金额")
    private BigDecimal selectedGoodsAmount;

    @ApiModelProperty("是否能勾选")
    private Boolean selectEnabled;

    @ApiModelProperty("是否选中")
    private Boolean selected;

    @ApiModelProperty("购物车数量")
    private Long cartGoodsNum;

    @ApiModelProperty("订单类型:1-pop,2-B2B")
    private Integer orderType;

    public static CartListVO empty() {
        CartListVO cartListVO = new CartListVO();
        cartListVO.setCartDistributorList(ListUtil.empty());
        cartListVO.setSelectedGoodsNum(0L);
        cartListVO.setSelectedGoodsAmount(BigDecimal.ZERO);
        return cartListVO;
    }
}

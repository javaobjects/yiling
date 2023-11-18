package com.yiling.f2b.web.cart.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.pojo.vo.SimpleGoodsVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 购物车商品 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CartGoodsVO extends SimpleGoodsVO {

    @ApiModelProperty("购物车ID")
    private Long cartId;

    @ApiModelProperty("配送商商品ID")
    private Long distributorGoodsId;

    @ApiModelProperty("是否能勾选")
    private Boolean selectEnabled;

    @ApiModelProperty("是否选中")
    private Boolean selected;

    @ApiModelProperty("销售单位")
    private String sellUnit;

    @ApiModelProperty("大件装")
    private Integer bigPackage;

    @ApiModelProperty("大件装备注")
    private String goodsRemark;

    @ApiModelProperty("单价")
    private BigDecimal price;

    @ApiModelProperty("数量")
    private Integer quantity;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("库存描述")
    private String stockText;

    @ApiModelProperty("库存数量")
    private Long stockNum;

    @ApiModelProperty("状态：状态：1-正常 2-商品,3-无货,4-失效")
    private Integer status;

    @ApiModelProperty("状态描述")
    private String statusText;
}

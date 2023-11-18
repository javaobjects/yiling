package com.yiling.f2b.web.order.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.pojo.vo.SimpleGoodsVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单商品信息 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderGoodsVO extends SimpleGoodsVO {

    @ApiModelProperty("购物车ID")
    private Long cartId;

    @ApiModelProperty("配送商商品ID")
    private Long distributorGoodsId;

    @ApiModelProperty("单价")
    private BigDecimal price;

    @ApiModelProperty("数量")
    private Integer quantity;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("大件装")
    private Integer bigPackage;

    @ApiModelProperty("大件装备注")
    private String goodsRemark;

}

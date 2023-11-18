package com.yiling.sales.assistant.app.order.vo;

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

    @ApiModelProperty("购物车ID/订单明细ID")
    private Long relationId;

    @ApiModelProperty("配送商商品ID")
    private Long distributorGoodsId;
    @ApiModelProperty("商品skuId")
    private Long goodsSkuId;

    @ApiModelProperty("单价")
    private BigDecimal price;

    @ApiModelProperty("数量")
    private Integer quantity;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("商品图片")
    private String goodPic;

    @ApiModelProperty("是否参与任务")
    private Boolean isHasTask;
}

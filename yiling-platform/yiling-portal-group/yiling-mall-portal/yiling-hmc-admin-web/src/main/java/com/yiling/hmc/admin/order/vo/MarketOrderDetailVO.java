package com.yiling.hmc.admin.order.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: benben.jia
 * @data: 2023/03/03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MarketOrderDetailVO extends BaseVO {

    @ApiModelProperty(value = "订单id", hidden = true)
    private Long orderId;

    @ApiModelProperty(value = "商品id", hidden = true)
    private Long goodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 商品规格
     */
    @ApiModelProperty("商品规格")
    private String goodsSpecification;

    /**
     * 商品单价
     */
    @ApiModelProperty("商品单价")
    private BigDecimal goodsPrice;

    /**
     * 购买数量
     */
    @ApiModelProperty("购买数量")
    private Integer goodsQuantity;

    /**
     * 商品图片
     */
    @ApiModelProperty("商品图片")
    private String pic;
}

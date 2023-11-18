package com.yiling.f2b.web.order.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 退货单明细表对应订单前端返回实体
 *
 * @author: yong.zhang
 * @date: 2022/2/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReturnOrderDetailVO extends BaseVO {

    @ApiModelProperty(value = " 订单ID")
    private Long orderId;

    @ApiModelProperty(value = " 订单号")
    private String orderNo;

    @ApiModelProperty(value = " 商品标准库ID")
    private Long standardId;

    @ApiModelProperty(value = " 配送商商品ID")
    private Long distributorGoodsId;

    @ApiModelProperty(value = " 商品ID")
    private Long goodsId;

    @ApiModelProperty(value = " 商品skuId")
    private Long goodsSkuId;

    @ApiModelProperty(value = " 商品编码")
    private String goodsCode;

    @ApiModelProperty(value = " 商品ERP编码")
    private String goodsErpCode;

    @ApiModelProperty(value = " 商品名称")
    private String goodsName;

    @ApiModelProperty(value = " 商品通用名")
    private String goodsCommonName;

    @ApiModelProperty(value = " 商品批准文号")
    private String goodsLicenseNo;

    @ApiModelProperty(value = " 商品规格")
    private String goodsSpecification;

    @ApiModelProperty(value = " 商品生产厂家")
    private String goodsManufacturer;

    @ApiModelProperty(value = " 商品单价")
    private BigDecimal goodsPrice;

    @ApiModelProperty(value = " 商品原始价格")
    private BigDecimal originalPrice;

    @ApiModelProperty(value = " 促销活动单价")
    private BigDecimal promotionGoodsPrice;

    @ApiModelProperty(value = " 购买数量")
    private Integer goodsQuantity;

    @ApiModelProperty(value = " 商品小计")
    private BigDecimal goodsAmount;

    @ApiModelProperty(value = " 现折金额")
    private BigDecimal cashDiscountAmount;

    @ApiModelProperty(value = " 促销活动ID(折扣价活动预留)")
    private Long promotionActivityId;

    @ApiModelProperty(value = " 促销活动类型:0无,2:特价,3:秒杀")
    private Integer promotionActivityType;
}

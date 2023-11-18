package com.yiling.sales.assistant.app.order.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 通用商品明细VO
 * @author:wei.wang
 * @date:2021/6/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderDetailVO extends BaseVO {

    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    /**
     * 商品标准库ID
     */
    @ApiModelProperty(value = "商品标准库ID")
    private Long standardId;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;


    /**
     * 商品批准文号
     */
    @ApiModelProperty(value = "商品批准文号")
    private String goodsLicenseNo;

    /**
     * 商品规格
     */
    @ApiModelProperty(value = "商品规格")
    private String goodsSpecification;

    /**
     * 商品生产厂家
     */
    @ApiModelProperty(value = "商品生产厂家")
    private String goodsManufacturer;

    /**
     * 商品单价
     */
    @ApiModelProperty(value = "商品单价")
    private BigDecimal goodsPrice;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Integer goodsQuantity;

    /**
     * 收货数量
     */
    @ApiModelProperty(value = "收货数量")
    private Integer receiveQuantity;

    /**
     * 商品小计
     */
    @ApiModelProperty(value = "金额小计")
    private BigDecimal goodsAmount;

    /**
     * 发货数量
     */
    @ApiModelProperty(value = "发货数量")
    private Integer deliveryQuantity;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String goodsPic;

    /**
     * 折扣金额
     */
    @ApiModelProperty(value = "折扣金额")
    private BigDecimal discountAmount;

    /**
     *支付金额
     */
    @ApiModelProperty(value = "支付金额")
    private BigDecimal realAmount;

}

package com.yiling.admin.pop.agreement.vo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/8/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("返利列表订单明细vo")
public class RebateOrderDetailPageListItemVO extends BaseVO {

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id", hidden = true)
    @JsonIgnore
    private Long       orderId;

    /**
     * 以岭商品Id
     */
    @ApiModelProperty(value = "以岭商品Id", hidden = true)
    @JsonIgnore
    private Long       goodsId;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String     orderNo;

    /**
     * 订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    @ApiModelProperty(value = "订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消")
    private Integer    orderStatus;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    @ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款")
    private Integer    paymentMethod;

    /**
     * 支付状态：1-待支付 2-已支付
     */
    @ApiModelProperty(value = "支付状态：1-待支付 2-已支付")
    private Integer    paymentStatus;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String     goodsName;

    /**
     * 商品批准文号
     */
    @ApiModelProperty(value = "商品批准文号")
    private String     goodsLicenseNo;

    /**
     * 商品规格
     */
    @ApiModelProperty(value = "商品规格")
    private String     goodsSpecification;

    /**
     * 商品购买总额
     */
    @ApiModelProperty(value = "商品购买总额")
    private BigDecimal goodsAmount;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Long       goodsQuantity;

    /**
     * 协议返还金额
     */
    @ApiModelProperty(value = "协议返还金额")
    private BigDecimal discountAmount;

    /**
     * 协议政策
     */
    @ApiModelProperty(value = "返利比例")
    private BigDecimal policyValue;

}

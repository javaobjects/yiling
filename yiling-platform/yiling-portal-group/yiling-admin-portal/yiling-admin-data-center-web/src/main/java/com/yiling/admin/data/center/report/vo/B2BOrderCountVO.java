package com.yiling.admin.data.center.report.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: wei.wang
 * @date: 2022-11-03
 */
@Data
public class B2BOrderCountVO {

    /**
     *成交金额
     */
    @ApiModelProperty("成交金额")
    private BigDecimal originalAmount;

    /**
     * 成交金额环比
     */
    @ApiModelProperty("成交金额环比")
    private BigDecimal originalAmountMonthRatio;

    /**
     * 成交金额同比
     */
    @ApiModelProperty("成交金额同比")
    private BigDecimal originalAmountYearRatio;

    /**
     * 优惠金额
     */
    @ApiModelProperty("优惠金额")
    private BigDecimal discountAmount;

    /**
     * 优惠金额环比
     */
    @ApiModelProperty("优惠金额环比")
    private BigDecimal  discountAmountMonthRatio;

    /**
     * 优惠金额同比
     */
    @ApiModelProperty("优惠金额同比")
    private BigDecimal  discountAmountYearRatio;

    /**
     * 订单数据
     */
    @ApiModelProperty("订单数据")
    private Integer orderQuantity;

    /**
     * 订单数据环比
     */
    @ApiModelProperty("订单数据环比")
    private BigDecimal  orderQuantityMonthRatio;

    /**
     * 订单数据同比
     */
    @ApiModelProperty("订单数据同比")
    private BigDecimal  orderQuantityYearRatio;

    /**
     * 下单客户数量
     */
    @ApiModelProperty("下单客户数量")
    private Integer  buyerQuantity;

    /**
     * 下单客户数量环比
     */
    @ApiModelProperty("下单客户数量环比")
    private BigDecimal  buyerQuantityMonthRatio;

    /**
     * 下单客户数量同比
     */
    @ApiModelProperty("下单客户数量同比")
    private BigDecimal  buyerQuantityYearRatio;

    /**
     * 销售供应商数量
     */
    @ApiModelProperty("销售供应商数量")
    private Integer sellerQuantity;

    /**
     * 销售供应商数量环比
     */
    @ApiModelProperty("销售供应商数量环比")
    private BigDecimal  sellerQuantityMonthRatio;

    /**
     * 销售供应商数量环比
     */
    @ApiModelProperty("销售供应商数量同比")
    private BigDecimal  sellerQuantityYearRatio;

}

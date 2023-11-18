package com.yiling.sjms.gb.vo;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品信息
 */
@Data
public class GbGoodsInfoVO {


    /**
     * 产品编码
     */
    @ApiModelProperty(value = "商品编码")
    private Long code;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String name;

    /**
     * 产品规格
     */
    @ApiModelProperty(value = "商品规格")
    private String specification;

    /**
     * 小包装
     */
    @ApiModelProperty(value = "小包装")
    private Integer smallPackage;

    /**
     * 团购数量（盒）
     */
    @ApiModelProperty(value = "团购数量（盒）")
    private Integer quantityBox;

    /**
     * 团购数量（件）
     */
    @ApiModelProperty(value = "团购数量（件）")
    private BigDecimal quantity;

    /**
     * 实际团购单价
     */
    @ApiModelProperty(value = "实际团购单价")
    private BigDecimal finalPrice;

    /**
     * 实际团购金额
     */
    @ApiModelProperty(value = "实际团购金额")
    private BigDecimal finalAmount;

    /**
     * 产品核算价
     */
    @ApiModelProperty(value = "产品核算价")
    private BigDecimal price;

    /**
     * 团购核算总额
     */
    @ApiModelProperty(value = "团购核算总额")
    private BigDecimal amount;

    /**
     * 团购成功回款日期
     */
    @ApiModelProperty(value = "团购成功回款日期")
    private Date paymentTime;

    /**
     * 团购成功回款日期
     */
    @ApiModelProperty(value = "流向月份")
    private Date flowMonth;

}

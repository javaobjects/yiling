package com.yiling.b2b.app.member.vo;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 会员订单优惠信息 VO
 */
@Data
@ApiModel("会员订单优惠信息VO")
public class OrderMemberDiscountVO {

    /**
     * 订单Id
     */
    @ApiModelProperty("订单Id")
    private Long id;

    /**
     * 卖家企业ID
     */
    @ApiModelProperty("卖家企业ID")
    private Long sellerEid;

    /**
     * 卖家企业名称
     */
    @ApiModelProperty("卖家企业名称")
    private String sellerEname;

    /**
     * 企业logo
     */
    @ApiModelProperty("企业logo")
    private String shopLogo;

    /**
     * 商品件数
     */
    @ApiModelProperty("商品件数")
    private Integer goodsQuantity;

    /**
     * 优惠金额
     */
    @ApiModelProperty("优惠金额")
    private BigDecimal DiscountAmount;

    /**
     * 下单时间
     */
    @ApiModelProperty("下单时间")
    private Date createTime;
}

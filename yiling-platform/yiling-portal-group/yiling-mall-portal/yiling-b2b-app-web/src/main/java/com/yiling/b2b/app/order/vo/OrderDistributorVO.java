package com.yiling.b2b.app.order.vo;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 配送商订单信息 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/23
 */
@Data
public class OrderDistributorVO {

    @ApiModelProperty("卖家企业ID")
    private Long distributorEid;

    @ApiModelProperty("卖家名称")
    private String distributorName;

    @ApiModelProperty("商品列表")
    private List<OrderGoodsVO> orderGoodsList;

    @ApiModelProperty("商品种数")
    private Long goodsSpeciesNum;

    @ApiModelProperty("商品件数")
    private Long goodsNum;

    @ApiModelProperty("商品总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("运费")
    private BigDecimal freightAmount;

    @ApiModelProperty("应付金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty(value = "买家留言")
    private String buyerMessage;

    @ApiModelProperty("是否是以岭配送商")
    private Boolean yilingFlag;

    @ApiModelProperty("支付方式")
    private List<PaymentMethodVO> paymentMethodList;

    @ApiModelProperty(value = "商家选择的支付方式", hidden = true)
    @JsonIgnore
    private Long paymentMethod;

    @ApiModelProperty(hidden = true, value = "商家劵优惠金额")
    @JsonIgnore
    private BigDecimal couponDiscountMoney;


    @ApiModelProperty(hidden = true, value = "平台劵优惠金额")
    @JsonIgnore
    private BigDecimal platformCouponDiscountMoney;

    @ApiModelProperty(value = "商家支付促销金额", hidden = true)
    @JsonIgnore
    private BigDecimal paymentDiscountMoney;

    @ApiModelProperty("支付促销显示类型:show-有促销活动,tip-提示促销信息,hidden-没有促销信息直接隐藏")
    private PaymentActivityShowTypeEnum paymentActivityShowType;

    @ApiModelProperty("是否可以使用商家优惠劵")
    private Boolean isUseShopCoupon;

    @ApiModelProperty("可用商家劵数量")
    private Integer shopCouponCount;

    @ApiModelProperty("商家劵Id")
    private Long customerShopCouponId;

    @ApiModelProperty("店铺优惠劵活动")
    private List<CouponActivityCanUseDetailVO> shopCouponActivity;

    @ApiModelProperty("店铺支付促销活动")
    private List<PaymentActivityDetailVO> shopPaymentActivity;

    /**
     * 预售优惠金额
     */
    @ApiModelProperty(value = "订单预售优惠金额", hidden = true)
    @JsonIgnore
    private BigDecimal presaleDiscountAmount = BigDecimal.ZERO;

    /**
     * 订单定金金额
     */
    @ApiModelProperty(value = "订单定金金额", hidden = true)
    @JsonIgnore
    private BigDecimal depositAmount = BigDecimal.ZERO;


    @Data
    public static class PaymentMethodVO {

        @ApiModelProperty("支付方式ID")
        private Long id;

        @ApiModelProperty("支付方式名称")
        private String name;

        @ApiModelProperty("是否可用")
        private Boolean enabled;

        @ApiModelProperty("是否选中")
        private Boolean selected;

        @ApiModelProperty(value = "支付优惠规则提示信息", example = "eg1:金额满100元减3元|数量满100件打5折扣")
        private String discountValueRules;

        @ApiModelProperty("不可用原因")
        private String disabledReason;
    }

}

package com.yiling.sales.assistant.app.order.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.order.order.dto.OrderDTO;

import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderConfirmVO {

    @ApiModelProperty(value = "买家名称")
    private String buyerName;

    @ApiModelProperty(value = "收货人姓名")
    private String name;

    @ApiModelProperty(value = "收货人电话")
    private String mobile;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty("下单时间")
    private Date orderTime;

    @ApiModelProperty("下单人")
    private String contacterName;

    @ApiModelProperty("联系人电话")
    private String contacterPhone;

    @ApiModelProperty("订单确认状态")
    private Boolean orderConfirmStatus;

    @ApiModelProperty("是否需要支付")
    private Boolean paymentStatus;

    @ApiModelProperty("平台劵优惠总金额")
    private BigDecimal platformCouponDiscountAmount;

    @ApiModelProperty("商家劵优惠总金额")
    private BigDecimal shopCouponDiscountAmount;

    @ApiModelProperty("配送商订单列表")
    private List<OrderSellerVO> orderDistributorList;

    @ApiModelProperty("平台劵优惠活动")
    private List<CouponActivityCanUseDetailVO> platformCouponActivity;

    @ApiModelProperty("商品种数")
    private Long goodsSpeciesNum;

    @ApiModelProperty("商品件数")
    private Long goodsNum;

    @ApiModelProperty("订单状态")
    private Integer orderStatus;

    @ApiModelProperty("商品总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("运费")
    private BigDecimal freightAmount;

    @ApiModelProperty("应付金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty("是否可以使用平台优惠劵")
    private Boolean isUsePlatformCoupon;

    @ApiModelProperty("可用平台劵数量")
    private Integer platformCouponCount;

    @ApiModelProperty("平台劵Id")
    private Long customerPlatformCouponId;


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

        @ApiModelProperty("不可用原因")
        private String disabledReason;
    }

    @Data
    public static class  OrderSellerVO {

        @ApiModelProperty("预订单号")
        private String orderNo;

        @ApiModelProperty("订单ID")
        private Long orderId;

        @ApiModelProperty("是否已选中")
        private Boolean selected;

        @ApiModelProperty("配送商企业ID")
        private Long distributorEid;

        @ApiModelProperty("配送商名称")
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

        @ApiModelProperty("支付方式")
        private List<PaymentMethodVO> paymentMethodList;

        @ApiModelProperty("是否可以使用商家优惠劵")
        private Boolean isUseShopCoupon;

        @ApiModelProperty("可用商家劵数量")
        private Integer shopCouponCount;

        @ApiModelProperty("商家劵Id")
        private Long customerShopCouponId;

        @ApiModelProperty("店铺平台劵优惠金额")
        private BigDecimal platformCouponDiscountMoney;

        @ApiModelProperty("店铺商家劵优惠金额")
        private BigDecimal shopCouponDiscountMoney;

        @ApiModelProperty("店铺优惠劵活动")
        private List<CouponActivityCanUseDetailVO> shopCouponActivity;

        @ApiModelProperty(value = "买家留言")
        private String buyerMessage;

    }

    public static OrderConfirmVO empty() {
        OrderConfirmVO pageVO = new OrderConfirmVO();
        pageVO.setOrderDistributorList(ListUtil.empty());
        pageVO.setGoodsSpeciesNum(0L);
        pageVO.setGoodsNum(0L);
        pageVO.setTotalAmount(BigDecimal.ZERO);
        pageVO.setFreightAmount(BigDecimal.ZERO);
        pageVO.setPaymentAmount(BigDecimal.ZERO);
        pageVO.setIsUsePlatformCoupon(false);
        pageVO.setPlatformCouponCount(0);
        pageVO.setCustomerPlatformCouponId(0l);
        pageVO.setPlatformCouponActivity(ListUtil.empty());
        pageVO.setPlatformCouponDiscountAmount(BigDecimal.ZERO);
        pageVO.setShopCouponDiscountAmount(BigDecimal.ZERO);
        pageVO.setOrderConfirmStatus(false);
        pageVO.setContacterName("");
        pageVO.setContacterPhone("");
        pageVO.setName("");
        pageVO.setAddress("");
        pageVO.setMobile("");

        return pageVO;
    }
}

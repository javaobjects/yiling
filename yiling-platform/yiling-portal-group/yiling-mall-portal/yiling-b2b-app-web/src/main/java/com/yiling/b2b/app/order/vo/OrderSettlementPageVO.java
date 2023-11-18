package com.yiling.b2b.app.order.vo;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import com.yiling.b2b.app.deliveryAddress.vo.DeliveryAddressVO;

import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单结算页 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/23
 */
@Data
public class OrderSettlementPageVO {

    @ApiModelProperty("默认收货地址信息")
    private DeliveryAddressVO deliveryAddressVO;

    @ApiModelProperty("卖家商品列表")
    private List<OrderDistributorVO> orderDistributorList;

    @ApiModelProperty("平台劵优惠活动")
    private List<CouponActivityCanUseDetailVO> platfromCouponActivity;

    @ApiModelProperty("平台支付优惠活动")
    private List<PaymentActivityDetailVO> platformPaymentActivity;


    @ApiModelProperty("平台支付促销显示类型:show-有促销活动,tip-提示促销信息,hidden-没有促销信息直接隐藏")
    private PaymentActivityShowTypeEnum paymentActivityShowType;

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

    @ApiModelProperty("是否可以使用平台优惠劵")
    private Boolean isUsePlatformCoupon;

    @ApiModelProperty("可用平台劵数量")
    private Integer platformCouponCount;

    @ApiModelProperty("平台劵Id")
    private Long customerPlatformCouponId;

    @ApiModelProperty("平台劵优惠总金额")
    private BigDecimal platformCouponDiscountAmount;

    @ApiModelProperty("商家劵优惠总金额")
    private BigDecimal shopCouponDiscountAmount;
    /**
     * 预售优惠金额
     */
    @ApiModelProperty("订单预售优惠金额")
    private BigDecimal presaleDiscountAmount = BigDecimal.ZERO;

    @ApiModelProperty("订单是否参与预售活动")
    private Boolean isHasPresaleActivity = false;

    /**
     * 预售活动信息
     */
    @ApiModelProperty("预售活动信息")
    private PresaleActivityInfoVO presaleActivityInfoVO;

    @ApiModelProperty("平台支付优惠总金额")
    private BigDecimal platformPaymentDiscountAmount;

    @ApiModelProperty("商家支付优惠总金额")
    private BigDecimal shopPaymentDiscountAmount;


    public static OrderSettlementPageVO empty() {
        OrderSettlementPageVO pageVO = new OrderSettlementPageVO();
        pageVO.setOrderDistributorList(ListUtil.empty());
        pageVO.setPlatformCouponCount(0);
        pageVO.setPlatformCouponDiscountAmount(BigDecimal.ZERO);
        pageVO.setPlatformPaymentDiscountAmount(BigDecimal.ZERO);
        pageVO.setGoodsSpeciesNum(0L);
        pageVO.setGoodsNum(0L);
        pageVO.setTotalAmount(BigDecimal.ZERO);
        pageVO.setFreightAmount(BigDecimal.ZERO);
        pageVO.setPaymentAmount(BigDecimal.ZERO);
        pageVO.setPresaleDiscountAmount(BigDecimal.ZERO);
        pageVO.setIsUsePlatformCoupon(false);
        pageVO.setPlatformCouponCount(0);
        pageVO.setShopPaymentDiscountAmount(BigDecimal.ZERO);
        pageVO.setShopCouponDiscountAmount(BigDecimal.ZERO);
        pageVO.setPlatformPaymentDiscountAmount(BigDecimal.ZERO);
        pageVO.setShopCouponDiscountAmount(BigDecimal.ZERO);
        pageVO.setPlatfromCouponActivity(Collections.emptyList());
        pageVO.setPaymentActivityShowType(PaymentActivityShowTypeEnum.hidden);

        return pageVO;
    }
}

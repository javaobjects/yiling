package com.yiling.b2b.app.order.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * B2B移动端明细
 * @author:wei.wang
 * @date:2021/10/20
 */
@Data
public class OrderDetailVO  {

    /**
     * 订单编码
     */
    @ApiModelProperty(value = "订单编码")
    private String orderNo;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String sellerEname;

    /**
     * 供应商id
     */
    @ApiModelProperty(value = "供应商id")
    private Long sellerEid;

    /**
     *采购商名称
     */
    @ApiModelProperty(value = "采购商名称")
    private String buyerEname;

    /**
     * 采购商id
     */
    @ApiModelProperty(value = "采购商id")
    private Long buyerEid;

    /**
     * 商品种类
     */
    @ApiModelProperty(value = "商品种类")
    private Integer goodsType;

    /**
     * 商品件数
     */
    @ApiModelProperty(value = "商品件数")
    private Integer goodsNumber;

    /**
     * 商品信息
     */
    @ApiModelProperty(value = "商品列表信息")
    private List<OrderGoodsVO> goodsList;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private Date createTime;

    /**
     * 流转信息
     */
    @ApiModelProperty(value = "流转信息")
    List<OrderLogVO> orderLogInfo;

    /**
     * 收货信息
     */
    @ApiModelProperty(value = "收货信息")
    private OrderAddressVO orderAddressVO;

    /**
     * 订单备注
     */
    @ApiModelProperty(value = "订单备注")
    private String orderNote;

    /**
     * 商品总金额
     */
    @ApiModelProperty(value = "商品总金额")
    private BigDecimal totalAmount;

    /**
     * 平台优惠劵金额
     */
    @ApiModelProperty(value = "平台优惠劵金额")
    private BigDecimal platformCouponDiscountAmount;

    /**
     * 商家优惠券金额
     */
    @ApiModelProperty(value = "商家优惠券金额")
    private BigDecimal couponDiscountAmount;

    /**
     * 预售优惠金额
     */
    @ApiModelProperty(value = "预售优惠金额")
    private BigDecimal presaleDiscountAmount;

    /**
     * 商家支付优惠金额
     */
    @ApiModelProperty(value = "商家支付优惠金额")
    private BigDecimal shopPaymentDiscountAmount;

    /**
     * 平台支付优惠总金额
     */
    @ApiModelProperty(value = "平台支付优惠总金额")
    private BigDecimal platformPaymentDiscountAmount;

    /**
     * 支付金额
     */
    @ApiModelProperty(value = "支付金额")
    private BigDecimal payAmount;

    /**
     * 实退金额
     */
    @ApiModelProperty(value = "实退金额")
    private BigDecimal realReturnAmount;

    /**
     * 还款金额
     */
    @ApiModelProperty(value = "还款金额")
    private BigDecimal repaymentAmount;

    /**
     * 还款时间
     */
    @ApiModelProperty(value = "还款时间")
    private Date repaymentTime;

    /**
     * 待还款金额
     */
    @ApiModelProperty(value = "待还款金额")
    private BigDecimal stayPaymentAmount;

    /**
     * 商品详细信息
     */
    @ApiModelProperty(value = "商品详细信息")
    List<OrderGoodsDetailVO> goodsDetailList;

    /**
     * 剩余时间
     */
    @ApiModelProperty(value = "还款剩余时间")
    private String remainTime;

    /**
     *  还款状态 1-待还款 2-已还款
     */
    @ApiModelProperty(value = "还款状态 ：1-待还款 2-已还款")
    private Integer repaymentStatus;

    /**
     *  还款状态 1-待还款 2-已还款
     */
    @ApiModelProperty(value = "状态 1-待付款 2-待发货 3-待收货 4-已完成 5-已取消")
    private Integer status;



    /**
     * 剩余收货天数
     */
    @ApiModelProperty(value = "剩余收货天数")
    private Long remainReceiveDay;

    @ApiModelProperty(value = "是否允许退货,0 允许，1不允许")
    private Integer isAllowReturn = 0;

    @ApiModelProperty(value = "不允许退货的原因")
    private String refuseReturnReason;

    /**
     * 是否展示取消按钮 true-显示 false-不显示
     */
    @ApiModelProperty(value = "是否展示取消按钮 true-显示 false-不显示")
    private Boolean cancelButtonFlag;

    /**
     * 定金金额
     */
    @ApiModelProperty(value = "定金金额")
    private BigDecimal depositAmount;

    /**
     * 尾款金额
     */
    @ApiModelProperty(value = "尾款金额")
    private BigDecimal balanceAmount;


    @ApiModelProperty(value = "预售支付按钮名称: 1-支付定金,2支付尾款")
    private Integer paymentNameType;

    /**
     * 订单类别
     * {@link com.yiling.order.order.enums.OrderCategoryEnum}
     */
    @ApiModelProperty(value = "订单类别:1-正常订单，2：预售订单")
    private Integer orderCategory;

    @ApiModelProperty(value = "支付尾款按钮控制:true:可以支付，false：不能支付")
    private Boolean presaleButtonFlag;

    /**
     * 预售尾款剩余时间
     */
    @ApiModelProperty(value = "预售尾款剩余时间")
    private String presaleRemainTime;


    /**
     * 是否展示再次购买按钮 true-显示 false-不显示
     */
    @ApiModelProperty(value = "是否允许再次购买,0 允许，1不允许")
    private Boolean againBuyButtonFlag;
}

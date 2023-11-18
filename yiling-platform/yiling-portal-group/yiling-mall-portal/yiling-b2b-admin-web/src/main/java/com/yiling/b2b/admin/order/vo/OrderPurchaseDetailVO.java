package com.yiling.b2b.admin.order.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 销售订单详情VO
 * @author:wei.wang
 * @date:2021/6/22
 */
@Data
public class OrderPurchaseDetailVO extends BaseVO {

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 买家名称
     */
    @ApiModelProperty(value = "买家企业id")
    private Long buyerEid;

    /**
     * 商品总金额
     */
    @ApiModelProperty(value = "货款总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "原价总金额")
    private BigDecimal originalAmount;

    /**
     * 应付金额
     */
    @ApiModelProperty(value = "支付总金额")
    private BigDecimal paymentAmount;

    /**
     * 优惠金额
     */
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountAmount;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    @ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款 4-线上支付")
    private Integer paymentMethod;

    /**
     * 支付状态：1-待支付 2-已支付
     */
    @ApiModelProperty(value = "支付状态：1-待支付 2-已支付")
    private Integer paymentStatus;


    /**
     * 订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    @ApiModelProperty(value = "订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消")
    private Integer orderStatus;

    /**
     * 下单备注
     */
    @ApiModelProperty(value = "下单备注")
    private String orderNote;


    @ApiModelProperty(value = "订单状态变化")
    private List<OrderLogVO> orderLogInfo;

    @ApiModelProperty(value = "商品信息")
    private List<OrderDetailDeliveryVO> orderDetailDelivery;


    /**
     * 物流类型：1-自有物流 2-第三方物流
     */
    @ApiModelProperty(value = "物流类型：1-自有物流 2-第三方物流")
    private Integer deliveryType;

    /**
     * 物流公司
     */
    @ApiModelProperty(value = "物流公司")
    private String deliveryCompany;

    /**
     * 物流单号
     */
    @ApiModelProperty(value = "物流单号")
    private String deliveryNo;

    /**
     * 剩余时间
     */
    @ApiModelProperty(value = "还款剩余时间")
    private String remainTime;

    @ApiModelProperty(value = "供应商企业信息")
    private EnterpriseInfoVO sellerEnterpriseInfo;

    /**
     * 剩余收货天数
     */
    @ApiModelProperty(value = "剩余收货天数")
    private Long remainReceiveDay;

    @ApiModelProperty(value = "是否展示取消按钮 true-显示 false-不显示")
    private Boolean cancelButtonFlag;

    @ApiModelProperty(value = "退货按钮 true-显示 false-不显示")
    private Boolean returnButtonFlag = false;

    @ApiModelProperty(value = "是否展示立即支付按钮 true-显示 false-不显示")
    private Boolean paymentButtonFlag = false;

    @ApiModelProperty(value = "是否展示收货按钮 true-显示 false-不显示")
    private Boolean receiveButtonFlag = false;

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

    @ApiModelProperty(value = "是否展示删除按钮 true-显示 false-不显示")
    private Boolean deleteButtonFlag = false;
}

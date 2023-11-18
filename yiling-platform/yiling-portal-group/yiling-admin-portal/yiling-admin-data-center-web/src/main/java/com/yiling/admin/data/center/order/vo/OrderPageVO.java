package com.yiling.admin.data.center.order.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单列表页面
 * @author:wei.wang
 * @date:2021/6/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderPageVO extends BaseVO {
    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 买家名称
     */
    @ApiModelProperty(value = "买家企业名称")
    private String buyerEname;

    /**
     *采购商Eid
     */
    @ApiModelProperty(value = "买家企业名称Eid")
    private Long buyerEid;

    /**
     * 省市区
     */
    @ApiModelProperty(value = "买家企业名称省市区")
    private String buyerAddress;

    /**
     * 卖家名称
     */
    @ApiModelProperty(value = "卖家名称")
    private String sellerEname;

    /**
     * 商品总金额
     */
    @ApiModelProperty(value = "货款金额")
    private BigDecimal totalAmount;

    /**
     * 应付金额
     */
    @ApiModelProperty(value = "应付金额")
    private BigDecimal paymentAmount;
    /**
     * 支付类型：1-线下支付 2-在线支付
     */
    @ApiModelProperty(value = "支付类型：1-线下支付 2-在线支付")
    private Integer paymentType;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    @ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款")
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
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private Date createTime;


    /**
     * 购买商品种类
     */
    @ApiModelProperty(value = "购买商品种类")
    private Integer goodsOrderNum;

    /**
     * 购买商品件数
     */
    @ApiModelProperty(value = "购买商品件数")
    private Integer goodsOrderPieceNum;

    /**
     * 发货商品种类
     */
    @ApiModelProperty(value = "发货商品种类")
    private Integer deliveryOrderNum;

    /**
     * 发货商品件数
     */
    @ApiModelProperty(value = "发货商品件数")
    private Integer deliveryOrderPieceNum;


    /**
     * 收获商品种类
     */
    @ApiModelProperty(value = "收获商品种类")
    private Integer receiveOrderNum;

    /**
     * 收获商品件数
     */
    @ApiModelProperty(value = "收获商品件数")
    private Integer receiveOrderPieceNum;


    /**
     * 发票金额
     */
    @ApiModelProperty(value = "发票金额")
    private BigDecimal invoiceAmount;

    /**
     * 状态：1-待申请 2-已申请 3-已开票 4-申请驳回 5-已作废
     */
    @ApiModelProperty(value = "发票状态：1-待申请 2-已申请 3-已开票 4-申请驳回 5-已作废")
    private Integer invoiceStatus;

    /**
     * 商品信息
     */
    @ApiModelProperty(value = "商品信息")
    private List<OrderDetailVO> goodOrderList;

    /**
     * 折扣总额
     */
    @ApiModelProperty(value = "折扣总额")
    private BigDecimal discountAmount;

    @ApiModelProperty(value = "原价总金额")
    private BigDecimal originalAmount;

    /**
     * 订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    @ApiModelProperty("订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台")
    private Integer orderSource;

    /**
     * 合同编号
     */
    @ApiModelProperty(value = "合同编号")
    private String contractNumber;

    /**
     * 优惠券使用信息
     */
    @ApiModelProperty(value = "优惠券使用信息")
    private String couponActivityInfo;

    /**
     * 是否有赠品 1：有 2：无"
     */
    @ApiModelProperty(value = "是否有赠品 0：无 1：有")
    private Integer haveGiftFlag;


    @ApiModelProperty("0无,2:特价,3:秒杀,4:组合促销 6:预售活动")
    private Integer activityType;

}

package com.yiling.sales.assistant.app.order.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.app.order.vo
 * @date: 2021/9/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderInfoVO extends BaseVO {

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
     * 卖家名称
     */
    @ApiModelProperty(value = "卖家名称")
    private String sellerEname;

    /**
     * 卖家id
     */
    @ApiModelProperty(value = "卖家id")
    private Long sellerEid;

    /**
     * 商品总金额
     */
    @ApiModelProperty(value = "货款总金额")
    private BigDecimal totalAmount;

    /**
     * 支付总金额
     */
    @ApiModelProperty(value = "支付总金额")
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
     * 订单状态:-50 -待提交,10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消 -30 待客户确认 -20 待付款
     */
    @ApiModelProperty(value = "订单状态：-50 -待提交 10-待审核 20-待发货 25-部分发货 30-已发货(B2B的待收货列表) 40-已收货 100-已完成 -10-已取消 -30 待客户确认 -20 待付款 ")
    private Integer orderStatus;

    /**
     * 审核状态：1-未提交 2-待审核 3-审核通过 4-审核驳回
     */
    private Integer auditStatus;

    /**
     * 审核驳回原因
     */
    private String auditRejectReason;

    /**
     * 配送商企业ID
     */
    private Long distributorEid;

    /**
     * 配送商名称
     */
    private String distributorEname;


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
     *折扣总金额
     */
    @ApiModelProperty(value = "折扣总金额")
    private BigDecimal discountAmount;

    /**
     * 下单备注
     */
    @ApiModelProperty(value = "下单备注")
    private String orderNote;


    @ApiModelProperty("商品列表")
    private  List<OrderDetailVO> goodDetailList;

}

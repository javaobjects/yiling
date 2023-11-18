package com.yiling.b2b.admin.order.vo;

import java.math.BigDecimal;
import java.util.Date;

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

    @ApiModelProperty(value = "原价总金额")
    private BigDecimal originalAmount;
    /**
     * 订单类别
     * {@link com.yiling.order.order.enums.OrderCategoryEnum}
     */
    @ApiModelProperty(value = "订单类别：1-正常订单 2-预售订单")
    private Integer orderCategory;

    /**
     * 支付总金额
     */
    @ApiModelProperty(value = "支付总金额")
    private BigDecimal paymentAmount;

    /**
     * 账期还款金额
     */
    @ApiModelProperty(value = "账期还款金额")
    private BigDecimal  paymentDayAmount;

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
     * 发货商品件数
     */
    @ApiModelProperty(value = "发货商品件数")
    private Integer deliveryOrderPieceNum;

    /**
     *折扣总金额
     */
    @ApiModelProperty(value = "优惠总金额")
    private BigDecimal discountAmount;

    /**
     * 是否有发货按钮
     */
    @ApiModelProperty(value = "是否有发货按钮: true-有 false-无")
    private Boolean judgeDeliveryFlag;

    /**
     * 是否有取消按钮
     */
    @ApiModelProperty(value = "是否有取消按钮: true-有 false-无")
    private Boolean judgeCancelFlag;


    /**
     * 是否有退货
     */
    @ApiModelProperty(value = "是否有退货: true-是 false-否")
    private Boolean orderReturnFlag;

    /**
     * 提交退货按钮是否存在
     */
    @ApiModelProperty(value = "提交退货按钮: true-是 false-否")
    private Boolean orderReturnSubmitFlag = true ;


    /**
     * 提交退货按钮是否存在
     */
    @ApiModelProperty(value = "确认回款按钮: true-是 false-否")
    private Boolean paymentDaySubmitFlag = false ;

    /**
     * ERP推送销售订单状态：1-未推送 2-推送成功 3-推送失败 4-eas提取成功 5-eas提取失败 6-读取成功
     */
    @ApiModelProperty(value = "ERP推送销售订单状态：1-未推送 2-推送成功 3-推送失败 4-eas提取成功 5-eas提取失败 6-读取成功")
    private Integer erpPushStatus;

}

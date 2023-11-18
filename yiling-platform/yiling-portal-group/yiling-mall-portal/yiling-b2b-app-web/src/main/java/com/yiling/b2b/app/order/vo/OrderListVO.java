package com.yiling.b2b.app.order.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * B2B移动端全部订单列表
 * @author:wei.wang
 * @date:2021/10/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderListVO extends BaseVO {

    /**
     * 订单编码
     */
    @ApiModelProperty(value = "订单编码")
    private String orderNo;

    /**
     *  状态 1-待付款 2-待发货 3-待收货 4-已完成 5-已取消
     */
    @ApiModelProperty(value = "状态 1-待付款 2-待发货 3-待收货 4-已完成 5-已取消")
    private Integer status;

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
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private Date createTime;

    /**
     * 支付金额
     */
    @ApiModelProperty(value = "支付金额")
    private BigDecimal payAmount;

    /**
     * 还款时间
     */
    @ApiModelProperty(value = "还款时间")
    private Date repaymentTime;

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
    @ApiModelProperty(value = "商品信息")
    private List<OrderGoodsVO> goodsList;

    /**
     * 是否展示取消按钮 true-显示 false-不显示
     */
    @ApiModelProperty(value = "是否展示取消按钮 true-显示 false-不显示")
    private Boolean cancelButtonFlag;

    @ApiModelProperty(value = "是否允许退货,0 允许，1不允许")
    private Integer isAllowReturn = 0;

    @ApiModelProperty(value = "不允许退货的原因")
    private String refuseReturnReason;

    @ApiModelProperty(value = "预售支付按钮名称: 1-支付定金,2支付尾款")
    private Integer paymentNameType;

    @ApiModelProperty(value = "支付尾款按钮控制:true:可以支付，false：不能支付")
    private Boolean presaleButtonFlag;

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


    /**
     * 订单类别
     * {@link com.yiling.order.order.enums.OrderCategoryEnum}
     */
    @ApiModelProperty(value = "订单类别:1-正常订单，2：预售订单")
    private Integer orderCategory;



    /**
     * 是否展示再次购买按钮 true-显示 false-不显示
     */
    @ApiModelProperty(value = "是否允许再次购买,true 允许，false 不允许")
    private Boolean againBuyButtonFlag;

}

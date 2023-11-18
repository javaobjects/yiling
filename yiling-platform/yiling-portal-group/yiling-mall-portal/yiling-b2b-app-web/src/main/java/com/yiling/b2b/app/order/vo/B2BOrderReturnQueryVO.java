package com.yiling.b2b.app.order.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 手机app查询售后信息的返回参数
 *
 * @author: yong.zhang
 * @date: 2021/10/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class B2BOrderReturnQueryVO extends BaseVO {

    @ApiModelProperty(value = "退货单id", required = true)
    private Long id;

    @ApiModelProperty(value = "退货单编号", required = true)
    private String returnNo;

    /**
     * 订单类别
     * {@link com.yiling.order.order.enums.OrderCategoryEnum}
     */
    @ApiModelProperty(value = "订单类别:1-正常订单，2：预售订单")
    private Integer orderCategory;

    @ApiModelProperty(value = "订单id", required = true)
    private Long orderId;

    @ApiModelProperty(value = "订单号", required = true)
    private String orderNo;

    @ApiModelProperty(value = "退货单状态： 1-待审核 2-已通过 3-已驳回", required = true)
    private Integer returnStatus;
    
    @ApiModelProperty(value = "驳回原因")
    private String failReason;

    @ApiModelProperty(value = "退货单备注", required = true)
    private String remark;

    @ApiModelProperty(value = "供应商id", required = true)
    private String sellerEid;

    @ApiModelProperty(value = "供应商名称", required = true)
    private String sellerEname;

    @ApiModelProperty(value = "提交时间", required = true)
    private Date createTime;

    @ApiModelProperty(value = "实退金额", required = true)
    private BigDecimal returnAmount;

    //=========================================================================

    @ApiModelProperty(value = "流转信息", required = true)
    private List<B2BOrderReturnLogQueryVO> orderReturnLogList;

    @ApiModelProperty(value = "退货单明细", required = true)
    private List<B2BOrderReturnDetailVO> returnDetailList;

    @ApiModelProperty(value = "退货明细种类数量", required = true)
    private Integer returnKind;

    @ApiModelProperty(value = "退货明细总数量", required = true)
    private Integer returnQuality;

    //=========================================================================

    @ApiModelProperty(value = "退回货款金额", required = true)
    private BigDecimal returnGoodsAmount;

    @ApiModelProperty(value = "退回平台优惠券金额", required = true)
    private BigDecimal returnB2BCouponAmount;

    @ApiModelProperty(value = "退回商家优惠券金额", required = true)
    private BigDecimal returnSellerCouponAmount;

    @ApiModelProperty(value = "退回预付款优惠金额", required = true)
    private BigDecimal returnSellerPresaleDiscountAmount;

    @ApiModelProperty(value = "退回平台支付优惠金额", required = true)
    private BigDecimal returnSellerPlatformPaymentDiscountAmount;

    @ApiModelProperty(value = "退回商家优惠券金额", required = true)
    private BigDecimal returnSellerShopPaymentDiscountAmount;
}

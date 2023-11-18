package com.yiling.admin.b2b.common.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PageOrderRefundVO extends BaseVO {

    @ApiModelProperty(value = "退款单号")
    private String refundNo;

    @ApiModelProperty(value = "退款状态1-待退款 2-退款中 3-退款成功")
    private Integer refundStatus;

    @ApiModelProperty(value = "退款失败原因")
    private String failReason;

    @ApiModelProperty(value = "卖家企业ID")
    private Long sellerEid;

    @ApiModelProperty(value = "卖家名称")
    private String sellerEname;

    @ApiModelProperty(value = "买家企业ID")
    private Long buyerEid;

    @ApiModelProperty(value = "买家名称")
    private String buyerEname;

    @ApiModelProperty(value = "退款类型1-订单取消退款 2-采购退货退款 3-商家驳回")
    private Integer refundType;

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "退货单号")
    private String returnNo;

    @ApiModelProperty(value = "支付方式")
    private String payWay;

    @ApiModelProperty(value = "商品总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "应付金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;

    @ApiModelProperty(value = "支付交易流水号")
    private String thirdTradeNo;

    @ApiModelProperty(value = "退款交易流水号")
    private String thirdFundNo;

    @ApiModelProperty(value = "退款单创建时间")
    private Date createTime;

    @ApiModelProperty(value = "操作人姓名")
    private String operateUserName;

    @ApiModelProperty(value = "操作时间")
    private Date operateTime;
}

package com.yiling.admin.b2b.common.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.b2b.admin.settlement.vo
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("重复支付Model")
public class RepeatPayOrderVO extends BaseVO {

    private static final long serialVersionUID=6094250982628974120L;
    /**
     * 交易ID
     */
    @ApiModelProperty(value = "交易ID")
    private String payId;

    /**
     * 商户支付流水号
     */
    @ApiModelProperty(value = "支付流水号")
    private String payNo;

    /**
     * 卖家Eid
     */
    @ApiModelProperty(value = "卖家Eid")
    private Long sellerEid;

    /**
     * 卖家名称
     */
    @ApiModelProperty(value = "卖家名称")
    private String sellerName;

    /**
     * 买家Eid
     */
    @ApiModelProperty(value = "买家Eid")
    private Long buyerEid;

    /**
     * 买家名称
     */
    @ApiModelProperty(value = "买家名称")
    private String buyerName;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称")
    private String userName;

    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID")
    private Long appOrderId;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String appOrderNo;

    /**
     * 支付订单状态(1:未付款,2:支付交易中，3:支付成功,4:交易取消)
     */
    @ApiModelProperty(value = "支付订单状态(1:未付款,2:支付交易中，3:支付成功,4:交易取消)")
    private Integer appOrderStatus;


    /**
     * 处理状态 1:未处理 2:退款中，3:退款失败，4:已处理
     */
    @ApiModelProperty(value = "处理状态 1:未处理 2:退款中，3:退款失败，4:已处理")
    private Integer dealState;

    /**
     * 支付成功时间
     */
    @ApiModelProperty(value = "支付时间")
    private Date payDate;

    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式")
    private String payWay;

    /**
     * 支付来源:app,pc
     */
    @ApiModelProperty(value = "支付来源:app,pc")
    private String paySource;

    /**
     * 交易类型(1:其他,2:支付,3:在线还款,4:转账,5:会员)
     */
    @ApiModelProperty(value = "交易类型(1:其他,2:支付,3:在线还款,4:转账,5:会员)")
    private Integer tradeType;

    /**
     * 支付金额
     */
    @ApiModelProperty(value = "支付金额")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "订单金额")
    private BigDecimal orderAmount;

    /**
     * 失败原因
     */
    @ApiModelProperty(value = "失败原因")
    private String errorMsg;

    /**
     * '退款状态(1:未退款,2:部分退款,3:已退款)'
     */
    @ApiModelProperty(value = "'退款状态(1:未退款,2:部分退款,3:已退款)'")
    private Integer refundState;

    /**
     * 第三方交易单号
     */
    @ApiModelProperty(value = "第三方交易单号")
    private String thirdTradeNo;

    /**
     * 已退款金额
     */
    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private Long updateUserId;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updateUserName;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "退款申请时间")
    private Date refundTime;

}

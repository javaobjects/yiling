package com.yiling.payment.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合并支付订单表
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-10-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("payment_merge_order")
public class PaymentMergeOrderDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 交易ID
     */
    private String payId;

    /**
     * 商户支付流水号
     */
    private String payNo;

    /**
     * 订单平台来源
     */
    private String orderPlatform;

    /**
     * 卖家Eid
     */
    private Long sellerEid;

    /**
     * 买家Eid
     */
    private Long buyerEid;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单ID
     */
    private Long appOrderId;

    /**
     * 订单号
     */
    private String appOrderNo;

    /**
     * 支付订单状态(1:未付款,2:支付交易中，3:支付成功,4:交易取消)
     */
    private Integer appOrderStatus;

    /**
     * 支付失效时间
     */
    private Date limitTime;

    /**
     * 支付成功时间
     */
    private Date payDate;

    /**
     * 支付方式
     */
    private String payWay;

    /**
     * 支付来源:app,pc
     */
    private String paySource;

    /**
     * 交易类型(1:尾款,2:支付,3:在线还款,4:定金,5:会员)
     */
    private Integer tradeType;

    /**
     * 货款金额(生产实际支付金额和货款金额一致)
     */
    private BigDecimal goodsAmount;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * '退款状态(1:未退款,2:部分退款,3:已退款)'
     */
    private Integer refundState;

    /**
     * 已退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 交易描述
     */
    private String content;

    /**
     * 是否重复支付0-不是 1-是
     */
    private Integer isDuplicate;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


}

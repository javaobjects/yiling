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
 * 订单支付重复表
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-10-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("payment_repeat_order")
public class PaymentRepeatOrderDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 退款流水单号
     */
    private String refundNo;

    /**
     * 交易单号
     */
    private String payNo;

    /**
     * 交易ID
     */
    private String payId;

    /**
     * 订单ID
     */
    private Long appOrderId;

    /**
     * 订单号
     */
    private String appOrderNo;

    /**
     * 第三方平台流水号
     */
    private String thirdTradeNo;

    /**
     * 第三方平台退款单号
     */
    private String thirdFundNo;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款状态(1:待退款,2:退款中,3:退款失败,4:退款成功,5:退款关闭)
     */
    private Integer refundState;

    /**
     * 退款处理方式：1，未退款通过接口退款，2，已退款标记已处理
     */
    private Integer methodType;
    /**
     * 退款时间
     */
    private Date refundDate;

    /**
     * 退款原因
     */
    private String reason;

    /**
     * 失败原因
     */
    private String errorMessage;

    /**
     * 操作人
     */
    private Long operateUser;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}

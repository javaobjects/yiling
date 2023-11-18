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
 * 企业付款记录表
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-11-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("payment_transfer_record")
public class PaymentTransferRecordDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业付款订单号
     */
    private String payNo;

    /**
     * 平台企业Eid
     */
    private Long eid;

    /**
     * 打款企业ID
     */
    private Long sellerEid;

    /**
     * 业务单据ID
     */
    private Long businessId;

    /**
     * 企业收款账户id
     */
    private Long receiptAccountId;

    /**
     * 收款账户
     */
    private String account;

    /**
     * 收款方开户名
     */
    private String accountName;

    /**
     * 银行编码
     */
    private String bankCode;

    /**
     * 支行编号
     */
    private String branchCode;

    /**
     * 付款金额
     */
    private BigDecimal payAmount;

    /**
     * 手续费
     */
    private BigDecimal feeCharge;

    /**
     * 手续费承担方： 1-平台 2-用户
     */
    private Integer feeChargeSide;

    /**
     * 支付公司返回的付款订单号
     */
    private String thirdTradeNo;

    /**
     * 支付渠道：yeePay
     */
    private String payWay;

    /**
     * 到账类型： 1-实时 2-两小时到账 3-次日到账
     */
    private Integer receiveType;

    /**
     * 交易状态：1-待支付 2-打款成功 3-已撤销,4-打款失败 5-银行处理中
     */
    private Integer tradeStatus;

    /**
     * 付款类型：1-结算
     */
    private Integer tradeType;

    /**
     * 交易时间
     */
    private Date tradeDate;

    /**
     * 交易描述
     */
    private String content;

    /**
     * 失败原因
     */
    private String errorMessage;

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

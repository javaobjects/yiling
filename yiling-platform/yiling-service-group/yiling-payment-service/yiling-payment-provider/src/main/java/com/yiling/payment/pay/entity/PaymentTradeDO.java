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
 * 交易订单记录表
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-10-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("payment_trade")
public class PaymentTradeDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 交易订单
     */
    private String payNo;

    /**
     * 交易ID
     */
    private String payId;

    /**
     * 交易时间
     */
    private Date tradeDate;

    /**
     * 交易失效时间
     */
    private Date limitTime;

    /**
     * 支付总金额
     */
    private BigDecimal payAmount;

    /**
     * 交易状态(1:待支付,2:交易成功,3:交易取消,4:支付失败)
     */
    private Integer tradeStatus;

    /**
     * 第三方返回订单流水号
     */
    private String thirdTradeNo;

    /**
     * 第三方支付状态
     */
    private String thirdStatus;

    /**
     * 第三方支付方式
     */
    private String payWay;

    /**
     * 支付来源:app,pc
     */
    private String paySource;

    /**
     * 操作平台。POP-PC平台：POP-PC；POP-APP平台：POP-APP；B2B-APP平台：B2B-APP；销售助手-APP平台：SA-APP；系统：sys
     */
    private String tradeSource;

    /**
     * 支付交易Ip
     */
    private String payIp;

    /**
     * 银行参数
     */
    private String bank;

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

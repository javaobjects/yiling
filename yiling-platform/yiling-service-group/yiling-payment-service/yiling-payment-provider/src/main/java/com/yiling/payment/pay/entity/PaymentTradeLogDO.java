package com.yiling.payment.pay.entity;

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
 * 支付交易日志记录表
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-10-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("payment_trade_log")
public class PaymentTradeLogDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 交易编号
     */
    private String payNo;

    /**
     * 退款交易编号
     */
    private String refundNo;

    /**
     * 日志类型(1,支付回调,2,退款回调，3,打款回调)
     */
    private Integer tradeType;

    /**
     * 同步回调日志记录
     */
    private String syncLog;

    /**
     * 第三方支付方式
     */
    private String payWay;

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

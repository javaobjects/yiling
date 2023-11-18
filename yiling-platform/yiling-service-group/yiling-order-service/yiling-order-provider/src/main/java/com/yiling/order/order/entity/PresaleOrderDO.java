package com.yiling.order.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 预售订单表
 * </p>
 *
 * @author zhigang.guo
 * @date 2022-10-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("presale_order")
public class PresaleOrderDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 订单Id
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /** {@Link com.yiling.order.order.enums.PreSaleActivityTypeEnum}
     *
     * 预售活动类型:1-定金预售 2-全额预售
     */
    private Integer activityType;

    /**
     * 是否已支付定金
     */
    private Integer isPayDeposit;

    /**
     * 是否已支付尾款
     */
    private Integer isPayBalance;

    /**
     * 定金金额
     */
    private BigDecimal depositAmount;

    /**
     * 尾款金额
     */
    private BigDecimal balanceAmount;

    /**
     * 尾款支付开始时间
     */
    private Date balanceStartTime;

    /**
     * 尾款支付结束时间
     */
    private Date balanceEndTime;

    /**
     * 是否发送支付短信
     */
    private Integer hasSendPaySms;

    /**
     * 是否已发送取消短信
     */
    private Integer hasSendCancelSms;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

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

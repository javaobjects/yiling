package com.yiling.user.payment.entity;

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
 * 账期账户日志
 * </p>
 *
 * @author tingwei.chen
 * @date 2021-07-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("payment_days_account_log")
public class PaymentDaysAccountLogDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 账期账户ID
     */
    private Long accountId;

    /**
     * 业务类型：1-支付 2-退款 3-还款 4-反审变更 5.临时额度 6.票折金额
     */
    private Integer businessType;

    /**
     * 业务单号
     */
    private String businessNo;

    /**
     * 额度变更值
     */
    private BigDecimal changedAmount;

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

    /**
     * 备注
     */
    private String remark;


}

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
 * 客户账期账户表
 * </p>
 *
 * @author gxl
 * @date 2021-05-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("payment_days_account")
public class PaymentDaysAccountDO extends BaseDO {

    private static final long serialVersionUID = 4434960345586799096L;

    /**
     * 企业id（供应商）
     */
    private Long eid;

    /**
     * 客户id（采购商）
     */
    private Long customerEid;

    /**
     * 供应商名称
     */
    private String ename;
    /**
     * 客户名称（采购商）
     */
    private String customerName;

    /**
     * 账期额度
     */
    private BigDecimal totalAmount;

    /**
     * 临时额度
     */
    private BigDecimal temporaryAmount;

    /**
     * 已使用额度
     */
    private BigDecimal usedAmount;

    /**
     * 历史已使用额度
     */
    private BigDecimal historyUseAmount;

    /**
     * 已还款额度
     */
    private BigDecimal repaymentAmount;

    /**
     * 历史已还款额度
     */
    private BigDecimal historyRepaymentAmount;

    /**
     * 可用额度
     */
    private BigDecimal availableAmount;

    /**
     * 类型：1-以岭 2-非以岭
     */
    private Integer type;

    /**
     * 还款周期（天）
     */
    private Integer period;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

    /**
     * 账期上浮点位（百分比）
     */
    private BigDecimal upPoint;

    /**
     * 是否长期有效：0-否 1-是
     */
    private Integer longEffective;

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
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

}

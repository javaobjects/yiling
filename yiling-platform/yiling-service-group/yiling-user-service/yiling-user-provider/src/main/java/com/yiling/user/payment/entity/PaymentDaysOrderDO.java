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
 * 账期使用订单信息
 * </p>
 *
 * @author tingwei.chen
 * @date 2021-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("payment_days_order")
public class PaymentDaysOrderDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 供应商名称
     */
    private String ename;
    /**
     * 供应商id
     */
    private Long eid;
    /**
     * 采购商id
     */
    private Long customerEid;
    /**
     * 采购商名称
     */
    private String customerName;

    /**
     * 账期账户ID
     */
    private Long accountId;

    /**
     * 账期（天）
     */
    private Integer period;

    /**
     * 使用金额
     */
    private BigDecimal usedAmount;

    /**
     * 使用时间
     */
    private Date usedTime;
    /**
     * 驳回退货单的退款金额
     */
    private BigDecimal returnAmount;

    /**
     * 到期时间
     */
    private Date expirationTime;

    /**
     * 还款状态：1-未还款 2-部分还款 3-全部还款
     */
    private Integer repaymentStatus;

    /**
     * 已还款金额
     */
    private BigDecimal repaymentAmount;

    /**
     * 还款时间
     */
    private Date repaymentTime;

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

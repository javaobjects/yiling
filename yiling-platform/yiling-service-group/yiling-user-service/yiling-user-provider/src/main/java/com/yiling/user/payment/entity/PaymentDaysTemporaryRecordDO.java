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
 * 账期临时额度记录
 * </p>
 *
 * @author tingwei.chen
 * @date 2021-07-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("payment_days_temporary_record")
public class PaymentDaysTemporaryRecordDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 账期账户ID
     */
    private Long accountId;
    /**
     * 企业ID（供应商）
     */
    private Long eid;

    /**
     * 企业名称（供应商）
     */
    private String ename;

    /**
     * 客户ID（采购商）
     */
    private Long customerEid;

    /**
     * 客户名称（采购商）
     */
    private String customerName;

    /**
     * 临时额度
     */
    private BigDecimal temporatyAmount;

    /**
     * 审核状态：1-待审核 2-审核通过 3-审核驳回
     */
    private Integer auditStatus;

    /**
     * 驳回原因
     */
    private String rejectReason;

    /**
     * 使用状态：1-未使用 2-已使用
     */
    private Integer useStatus;

    /**
     * 使用时间
     */
    private Date useTime;

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

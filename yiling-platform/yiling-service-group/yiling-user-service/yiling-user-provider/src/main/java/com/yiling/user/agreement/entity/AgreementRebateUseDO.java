package com.yiling.user.agreement.entity;

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
 * 返利申请使用表
 * </p>
 *
 * @author dexi.yao
 * @date 2021-07-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("agreement_rebate_use")
public class AgreementRebateUseDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String entName;

    /**
     * 企业账号
     */
    private String account;

    /**
     * 申请单号
     */
    private String applyCode;

    /**
     * 返利类型 1- 票折 2- 现金 3-冲红 4-健康城卡
     */
    private Integer rebateType;

    /**
     * 返利金额
     */
    private BigDecimal amount;

    /**
     * 申请时间
     */
    private Date applyTime;

    /**
     * 审核状态
     */
    private Integer status;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

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

    /**
     * 备注
     */
    private String remark;


}

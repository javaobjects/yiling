package com.yiling.user.enterprise.entity;

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
 * 企业采购申请 DO
 * </p>
 *
 * @author lun.yu
 * @date 2022-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("enterprise_purchase_apply")
public class EnterprisePurchaseApplyDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 采购商企业ID
     */
    private Long customerEid;

    /**
     * 供应商企业ID
     */
    private Long eid;

    /**
     * 审核状态：1-待审核 2-已建采 3-已驳回
     */
    private Integer authStatus;

    /**
     * 审核人
     */
    private Long authUser;

    /**
     * 审核时间
     */
    private Date authTime;

    /**
     * 驳回原因
     */
    private String authRejectReason;

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

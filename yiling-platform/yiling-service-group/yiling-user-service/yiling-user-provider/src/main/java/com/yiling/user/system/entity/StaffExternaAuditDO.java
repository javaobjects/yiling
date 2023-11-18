package com.yiling.user.system.entity;

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
 * 外部员工账户审核信息
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-01-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("staff_externa_audit")
public class StaffExternaAuditDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 身份证正面照KEY
     */
    private String idCardFrontPhotoKey;

    /**
     * 身份证反面照KEY
     */
    private String idCardBackPhotoKey;

    /**
     * 销售区域是否全国：0-否 1-是
     */
    private Integer salesAreaAllFlag;

    /**
     * 销售区域描述
     */
    private String salesAreaDesc;

    /**
     * 销售区域Json串
     */
    private String salesAreaJson;

    /**
     * 审核状态：1-待审核 2-审核通过 3-审核驳回
     */
    private Integer auditStatus;

    /**
     * 审核驳回原因
     */
    private String auditRejectReason;

    /**
     * 审核人ID
     */
    private Long auditUserId;

    /**
     * 审核人姓名
     */
    private String auditUserName;

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

package com.yiling.admin.sales.assistant.audit.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 外部员工审核信息列表项VO
 *
 * @author: xuan.zhou
 * @date: 2022/1/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StaffExternaAuditInfoVO extends BaseVO {

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
     * 身份证正面照URL
     */
    private String idCardFrontPhotoUrl;

    /**
     * 身份证反面照URL
     */
    private String idCardBackPhotoUrl;

    /**
     * 审核状态（参考字典：staff_external_audit_status）
     */
    private Integer auditStatus;

    /**
     * 审核驳回原因
     */
    private String auditRejectReason;

    /**
     * 审核人姓名
     */
    private String auditUserName;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 创建时间
     */
    private Date createTime;
}

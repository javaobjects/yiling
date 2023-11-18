package com.yiling.admin.sales.assistant.audit.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 审核外部用户个人信息 Form
 *
 * @author: xuan.zhou
 * @date: 2022/1/21
 */
@Data
public class AuditStaffExternaInfoForm {

    @NotNull(groups = { AuditPassValidateGroup.class, AuditRejectValidateGroup.class })
    @ApiModelProperty(value = "ID")
    private Long id;

    @NotNull(groups = { AuditPassValidateGroup.class, AuditRejectValidateGroup.class })
    @Range(min = 2, max = 3, groups = { AuditPassValidateGroup.class, AuditRejectValidateGroup.class })
    @ApiModelProperty(value = "审核状态：2-审核通过 3-审核驳回", required = true)
    private Integer auditStatus;

    @NotEmpty(groups = { AuditRejectValidateGroup.class })
    @ApiModelProperty(value = "审核驳回原因")
    private String auditRejectReason;

    public interface AuditPassValidateGroup {
    }

    public interface AuditRejectValidateGroup {
    }
}

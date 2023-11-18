package com.yiling.user.system.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 审核外部用户个人信息 Request
 *
 * @author: xuan.zhou
 * @date: 2022/1/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AuditStaffExternaInfoRequest extends BaseRequest {

    /**
     * ID
     */
    @NotNull
    private Long id;

    /**
     * 审核状态：2-审核通过 3-审核不通过
     */
    @NotNull
    private Integer auditStatus;

    /**
     * 审核驳回原因
     */
    private String auditRejectReason;
}

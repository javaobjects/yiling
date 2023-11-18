package com.yiling.sjms.form.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 驳回 Request
 *
 * @author: xuan.zhou
 * @date: 2023/2/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RejectFormRequest extends BaseRequest {

    private static final long serialVersionUID = -6265497967480319297L;

    /**
     * 基础表单ID
     */
    @NotNull
    private Long id;

    /**
     * 驳回原因（非必填）
     */
    private String rejectReason;
}

package com.yiling.sjms.form.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2023/2/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateRemarkRequest extends BaseRequest {

    private static final long serialVersionUID = 3038793312475430419L;

    /**
     * 基础表单ID
     */
    @NotNull
    private Long id;

    private String remark;
}
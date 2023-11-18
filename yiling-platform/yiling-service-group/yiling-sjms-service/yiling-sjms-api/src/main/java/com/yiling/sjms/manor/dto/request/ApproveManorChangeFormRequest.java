package com.yiling.sjms.manor.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 审批通过变更子表单
 *
 * @author:gxl
 * @date: 2023/5/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ApproveManorChangeFormRequest extends BaseRequest {


    /**
     * 基础表单ID
     */
    @NotNull
    private Long id;

    /**
     * 工号
     */
    private String empId;

}

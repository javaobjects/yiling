package com.yiling.sjms.agency.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ApproveToAgLockRequest extends BaseRequest {

    private static final long serialVersionUID = -5693721255423215938L;

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

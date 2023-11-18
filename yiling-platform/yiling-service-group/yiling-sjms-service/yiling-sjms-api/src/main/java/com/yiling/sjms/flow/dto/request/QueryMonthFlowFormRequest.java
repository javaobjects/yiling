package com.yiling.sjms.flow.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2023/6/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMonthFlowFormRequest extends BaseRequest {
    private static final long serialVersionUID = -4261042878382598137L;
    /**
     * 主流程表单id
     */
    private Long formId;

    /**
     * 数据检查状态 1 通过 2未通过 3检查中
     */
    private Integer checkStatus;

}
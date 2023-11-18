package com.yiling.sales.assistant.task.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 任务配送商移除
 * @author gxl
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteTaskDistributorRequest extends BaseRequest {

    private static final long serialVersionUID = -7173002996287365781L;
    private Long taskDistributorId;

}

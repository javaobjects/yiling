package com.yiling.dataflow.crm.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RemoveManorRequest extends BaseRequest {
    private Long id;
}

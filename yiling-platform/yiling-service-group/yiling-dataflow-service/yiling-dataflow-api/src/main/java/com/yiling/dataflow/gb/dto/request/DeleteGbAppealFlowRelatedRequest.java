package com.yiling.dataflow.gb.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/6/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteGbAppealFlowRelatedRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;
}

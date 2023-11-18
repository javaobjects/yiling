package com.yiling.dataflow.gb.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/5/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteGbAppealFlowAllocationRequest extends BaseRequest {

    /**
     * 团购处理结果主键ID列表
     */
    private List<Long> idList;

}

package com.yiling.dataflow.gb.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/5/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGbAppealFormFlowStatisticPageRequest extends QueryPageListRequest {

    /**
     * 团购申诉申请ID
     */
    private Long appealFormId;

}

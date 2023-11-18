package com.yiling.dataflow.flowcollect.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询月流向销售分页列表 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowMonthPageRequest extends QueryPageListRequest {

    /**
     * 任务ID
     */
    @NotNull
    private Long taskId;

}

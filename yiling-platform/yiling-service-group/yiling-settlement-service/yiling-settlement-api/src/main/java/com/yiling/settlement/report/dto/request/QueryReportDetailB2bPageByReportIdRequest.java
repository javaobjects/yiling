package com.yiling.settlement.report.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryReportDetailB2bPageByReportIdRequest extends QueryPageListRequest {

    /**
     * 报表id
     */
    @NotNull
    private List<Long> reportIdList;
}
